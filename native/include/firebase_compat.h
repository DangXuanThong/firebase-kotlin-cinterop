#ifndef FIREBASE_COMPAT_H
#define FIREBASE_COMPAT_H

#ifdef __cplusplus
extern "C" {
#endif

typedef struct NativeAppOptions NativeAppOptions; // Wrapper of firebase::AppOptions
typedef struct NativeApp NativeApp; // Wrapper of firebase::App

NativeAppOptions* appOptions_create(
    const char* app_id,
    const char* api_key,
    const char* fcm_sender_id,
    const char* database_url,
    const char* ga_tracking_id,
    const char* storage_bucket,
    const char* project_id
);

NativeAppOptions* appOptions_from_json(const char* config);

void appOptions_destroy(NativeAppOptions* options);

#ifdef __cplusplus
}
#endif

#endif // FIREBASE_COMPAT_H
