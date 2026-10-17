#ifndef FIREBASE_COMPAT_H
#define FIREBASE_COMPAT_H

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
const NativeAppOptions* AppOptions_fromJson(const char* config);
void AppOptions_destroy(NativeAppOptions* options);

// App
// Getters
const char* App_getName(const NativeApp* app);
const NativeAppOptions* App_getOptions(const NativeApp* app);
// Methods
const NativeApp* App_create(const NativeAppOptions* options, const char* name);
const NativeApp* App_getInstance(const char* name);
void App_destroy(NativeApp* app);

#ifdef __cplusplus
}
#endif

#endif // FIREBASE_COMPAT_H
