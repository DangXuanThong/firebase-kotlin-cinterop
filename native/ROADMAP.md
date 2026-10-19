# 🗺️ Native C Compat Layer Roadmap (`app.h` Core Focus)

This document outlines the phased roadmap for porting Google's official **Firebase C++ SDK** (`app.h`, `log.h`, `future.h`) into the C-ABI compatibility layer (`libfirebase_native`).

```text
  Phase 1: Core App Initialization & Lifecycle (Must-Have)
    └── AppOptions -> App::Create -> App::GetInstance -> App::Destroy
  
  Phase 2: Logging & Diagnostics
    └── SetLogLevel -> GetLogLevel
  
  Phase 3: Async Future Callback Bridge (Foundation for Auth & Firestore)
    └── Future OnCompletion Callback Bridge
  
  Phase 4: Multi-App Management & Diagnostics
    └── GetApps -> InitResult Handling
```

---

## 📍 Phase 1: Core App Initialization & Lifecycle (Highest Priority)

*Goal: Enable creating, configuring, inspecting, and destroying `FirebaseApp` instances.*

### 1.1 `NativeAppOptions` (Configuration)
- [x] **`AppOptions_create(...)`**: Create options from manual properties (`app_id`, `api_key`, `project_id`, `database_url`, `storage_bucket`, `fcm_sender_id`, `ga_tracking_id`).
- [x] **`AppOptions_fromJson(const char* json_text)`**: Create options directly from `google-services.json` string using `firebase::AppOptions::LoadFromJsonConfig`.
- [x] **`AppOptions_destroy(NativeAppOptions*)`**: Memory cleanup function for `NativeAppOptions`.
- ~~**`AppOptions_get_*`**~~: Not needed (Kotlin's `FirebaseOptions` data class manages options state in memory on the Kotlin side).

### 1.2 `NativeApp` (Core App Instance)
- [x] **`App_create(options, name)`**: Wraps `firebase::App::Create(options, name)`. If `name` is `NULL`, creates default `"[DEFAULT]"` app.
- [x] **`App_getDefault()`**: Wraps `firebase::App::GetInstance()`.
- [x] **`App_getInstance(const char* name)`**: Wraps `firebase::App::GetInstance(name)`.
- [x] **`App_getName(NativeApp*)`**: Returns the app name string (`app->name()`).
- [x] **`App_getOptions(NativeApp*)`**: Retrieves copy of options used by app.
- [x] **`App_destroy(NativeApp*)`**: Safely deletes and cleans up the `firebase::App` instance.

---

## 📍 Phase 2: Logging & Diagnostics

*Goal: Allow developers to control C++ SDK log verbosity for debugging native behavior.*

### 2.1 Log Level Configuration
- [x] **`fdb_set_log_level(int level)`**: Wraps `firebase::SetLogLevel()`.
  - `0`: Verbose
  - `1`: Debug
  - `2`: Info
  - `3`: Warning
  - `4`: Error
  - `5`: Assert
- [x] **`fdb_get_log_level()`**: Wraps `firebase::GetLogLevel()`.

---

## 📍 Phase 3: Async `Future` Callback Bridge (`future.h`)

*Goal: Build the core C callback mechanism that turns `firebase::Future<T>` into Kotlin `suspend` coroutines.*

Every async operation in Firebase C++ (Auth, Firestore, Storage) returns a `firebase::Future<T>`. Before writing Auth or Firestore bindings, build this generic callback helper in `future_bridge.hpp`:

- [x] **3.1 C Callback Signatures (`firebase_compat.h`)**
```c
typedef void (*FutureSuccessCallback)(
    void* user_data, 
    const uint8_t* payload, 
    size_t payload_len
);

typedef void (*FutureFailureCallback)(
    void* user_data, 
    int error_code, 
    const char* error_message
);
```

- [x] **3.2 C++ Future Helper (`future_bridge.hpp`)**
```cpp
namespace future_bridge {
    inline std::vector<uint8_t> to_cbor(const nlohmann::json& j);

    // Overloads for Future<void>, custom serializer, and default ADL to_json:
    template <typename T, typename Serializer>
    void Future_bindCallback(
        const firebase::Future<T>& future, 
        FutureSuccessCallback on_success, 
        FutureFailureCallback on_failure, 
        void* user_data, 
        Serializer serializer
    );
}
```

---

## 📍 Phase 4: Multi-App Management & Diagnostics

*Goal: Support apps running multiple simultaneous Firebase projects.*

- [x] **`fdb_app_get_all_apps()`**: Wraps `firebase::App::GetApps()` to return a list of all active app names.
- [ ] **`fdb_init_result_to_string(int init_result)`**: Converts `firebase::InitResult` enum errors (e.g. `kInitResultFailedMissingDependency`) into human-readable C strings for Kotlin exceptions.
