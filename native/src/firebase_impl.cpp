#include <vector>
#include "firebase_compat.h"
#include "firebase/app.h"

using namespace firebase;

extern "C" {

// AppOptions getters
const char* AppOptions_getAppId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->app_id();
}
const char* AppOptions_getApiKey(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->api_key();
}
const char* AppOptions_getFcmSenderId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->messaging_sender_id();
}
const char* AppOptions_getDatabaseUrl(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->database_url();
}
const char* AppOptions_getGaTrackingId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->ga_tracking_id();
}
const char* AppOptions_getStorageBucket(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->storage_bucket();
}
const char* AppOptions_getProjectId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->project_id();
}

// AppOption methods
NativeAppOptions* AppOptions_create(
    const char* app_id,
    const char* api_key,
    const char* fcm_sender_id,
    const char* database_url,
    const char* ga_tracking_id,
    const char* storage_bucket,
    const char* project_id
) {
  auto* options = new AppOptions();

  if (app_id) options->set_app_id(app_id);
  if (api_key) options->set_api_key(api_key);
  if (fcm_sender_id) options->set_messaging_sender_id(fcm_sender_id);
  if (database_url) options->set_database_url(database_url);
  if (ga_tracking_id) options->set_ga_tracking_id(ga_tracking_id);
  if (storage_bucket) options->set_storage_bucket(storage_bucket);
  if (project_id) options->set_project_id(project_id);

  return options;
}

const NativeAppOptions* AppOptions_fromJson(const char* config) {
  if (!config || config[0] == '\0') return nullptr;
  return AppOptions::LoadFromJsonConfig(config);
}

void AppOptions_destroy(NativeAppOptions* options) {
  delete options;
}

// App getters
const char* App_getName(const NativeApp* app) {
  if (!app) return nullptr;
  return app->name();
}
const NativeAppOptions* App_getOptions(const NativeApp* app) {
  if (!app) return nullptr;
  return &app->options();
}

// App methods
const NativeApp* App_create(const NativeAppOptions* options, const char* name) {
  if (!options) return nullptr;
  return (!name || name[0] == '\0') ? App::Create(*options)
                                    : App::Create(*options, name);
}

const NativeApp* App_getInstance(const char* name) {
  return (!name || name[0] == '\0') ? App::GetInstance() : App::GetInstance(name);
}

size_t App_getAppsCount(void) {
  return App::GetApps().size();
}

size_t App_getApps(NativeApp** out, size_t capacity) {
  if (!out) return 0;
  const auto apps = App::GetApps();
  size_t size = apps.size() < capacity ? apps.size() : capacity;
  for (size_t i = 0; i < size; ++i) out[i] = apps[i];
  return size;
}

void App_destroy(NativeApp* app) {
  delete app;
}

} // extern "C"
