#pragma once

#include <vector>
#include <exception>
#include <nlohmann/json.hpp>
#include "firebase_compat.h"
#include "firebase/future.h"

namespace future_bridge {

/// Serializes a json object into CBOR binary format.
inline std::vector<uint8_t> to_cbor(const nlohmann::json& j) {
  return nlohmann::json::to_cbor(j);
}

///Binds completion callbacks to a firebase::Future<void>.
///
///@param future The async future to observe.
///@param on_success Callback invoked when future completes without error.
///@param on_failure Callback invoked when future fails with SDK error code and message.
///@param user_data Opaque pointer passed through to the completion handler.
inline void Future_bindCallback(
  const firebase::Future<void>& future,
  FutureSuccessCallback on_success,
  FutureFailureCallback on_failure,
  void* user_data
) {
  future.OnCompletion([on_success, on_failure, user_data](const firebase::Future<void>& completed) {
    int error_code = completed.error();
    if (error_code == 0) {
      if (on_success) on_success(user_data, nullptr, 0);
    } else {
      if (on_failure) on_failure(user_data, error_code, completed.error_message());
    }
  });
}

/// Binds completion callbacks to a typed firebase::Future<T> using a custom serializer.
///
/// @tparam T Result type of the future.
/// @tparam Serializer Functor or lambda with signature (nlohmann::json& j, const T& val).
/// @param future The async future to observe.
/// @param on_success Callback invoked when future completes successfully, providing CBOR binary bytes.
/// @param on_failure Callback invoked on SDK failure or serialization failure (-2).
/// @param user_data Opaque pointer passed through to the completion handler.
/// @param serializer Custom serializer mapping T to nlohmann::json.
template<typename T, typename Serializer>
inline void Future_bindCallback(
  const firebase::Future <T>& future,
  FutureSuccessCallback on_success,
  FutureFailureCallback on_failure,
  void* user_data,
  Serializer serializer
) {
  future.OnCompletion([on_success, on_failure, user_data, serializer](const firebase::Future <T>& completed) {
    int error_code = completed.error();
    if (error_code != 0) {
      if (on_failure) on_failure(user_data, error_code, completed.error_message());
      return;
    }

    const T* res = completed.result();
    if (!res) {
      if (on_success) on_success(user_data, nullptr, 0);
      return;
    }

    try {
      nlohmann::json j;
      serializer(j, *res);
      std::vector <uint8_t> cbor = nlohmann::json::to_cbor(j);
      if (on_success) on_success(user_data, cbor.data(), cbor.size());
    } catch (const std::exception& e) {
      // -2 indicates serialization failure
      if (on_failure) on_failure(user_data, -2, e.what());
    }
  });
}

/// Binds completion callbacks to a typed firebase::Future<T> using default nlohmann::json conversion.
///
/// @tparam T Result type of the future that has adl_serializer or to_json defined.
/// @param future The async future to observe.
/// @param on_success Callback invoked when future completes successfully, providing CBOR binary bytes.
/// @param on_failure Callback invoked on SDK failure or serialization failure (-2).
/// @param user_data Opaque pointer passed through to the completion handler.
template<typename T>
inline void Future_bindCallback(
  const firebase::Future <T>& future,
  FutureSuccessCallback on_success,
  FutureFailureCallback on_failure,
  void* user_data
) {
  Future_bindCallback(
    future,
    on_success,
    on_failure,
    user_data,
    [](nlohmann::json& j, const T& val) {j = val;}
  );
}

} // namespace future_bridge
