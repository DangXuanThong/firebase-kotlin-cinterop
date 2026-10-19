#ifndef FIREBASE_COMPAT_H
#define FIREBASE_COMPAT_H

#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
namespace firebase { class App; class AppOptions; }
typedef firebase::App NativeApp;
typedef firebase::AppOptions NativeAppOptions;
extern "C" {
#else
typedef struct NativeApp NativeApp;
typedef struct NativeAppOptions NativeAppOptions;
#endif

// AppOptions
// Getters
const char* AppOptions_getAppId(const NativeAppOptions* options);
const char* AppOptions_getApiKey(const NativeAppOptions* options);
const char* AppOptions_getFcmSenderId(const NativeAppOptions* options);
const char* AppOptions_getDatabaseUrl(const NativeAppOptions* options);
const char* AppOptions_getGaTrackingId(const NativeAppOptions* options);
const char* AppOptions_getStorageBucket(const NativeAppOptions* options);
const char* AppOptions_getProjectId(const NativeAppOptions* options);
// Methods
NativeAppOptions* AppOptions_create(
    const char* app_id,
    const char* api_key,
    const char* fcm_sender_id,
    const char* database_url,
    const char* ga_tracking_id,
    const char* storage_bucket,
    const char* project_id
);
NativeAppOptions* AppOptions_fromJson(const char* config);
void AppOptions_destroy(NativeAppOptions* options);

// App
// Getters
const char* App_getName(const NativeApp* app);
const NativeAppOptions* App_getOptions(const NativeApp* app);
const char* App_getDefaultAppName();
// Methods
NativeApp* App_create(const NativeAppOptions* options, const char* name);
NativeApp* App_getInstance(const char* name);
size_t App_getAppsCount(void);
size_t App_getApps(NativeApp** out, size_t capacity);
void App_destroy(NativeApp* app);

// Logging
typedef enum {
    LOG_LEVEL_VERBOSE = 0,
    LOG_LEVEL_DEBUG = 1,
    LOG_LEVEL_INFO = 2,
    LOG_LEVEL_WARNING = 3,
    LOG_LEVEL_ERROR = 4,
    LOG_LEVEL_ASSERT = 5
} NativeLogLevel;
void setLogLevel(int level);
int getLogLevel(void);

// Async Callbacks
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

#ifdef __cplusplus
}
#endif

#endif // FIREBASE_COMPAT_H
