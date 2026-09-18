// kn_bridge.h — a portable C callback in place of firebase_ffi's
// Dart_Port_DL delivery, so bindings can be called from any C ABI
// consumer (here: Kotlin/Native cinterop), not just a Dart VM.
//
// Contract: `payload` is valid only for the duration of the callback call.
// The receiver must copy whatever it needs before returning — there is no
// ownership handoff, so nothing needs to be freed on the caller's side.

#pragma once
#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

// seq keeps the same meaning firebase_ffi already used:
//   1  : success. len == 0 means "document does not exist".
//  -1  : the SDK operation itself failed.
//  -2  : it succeeded, but the result failed to serialize.
typedef void (*FdbCallback)(void* userdata, int64_t seq,
                            const uint8_t* payload, size_t len);

#ifdef __cplusplus
}  // extern "C"
#endif
