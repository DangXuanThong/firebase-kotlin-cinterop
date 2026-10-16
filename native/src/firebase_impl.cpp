#include "firebase_compat.h"
#include "firebase/app.h"

using namespace firebase;

struct NativeAppOptions {
  AppOptions delegate;
};

struct NativeApp {
  App* delegate;
};

extern "C" {

// AppOptions getters
const char* AppOptions_getAppId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.app_id();
}
const char* AppOptions_getApiKey(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.api_key();
}
const char* AppOptions_getFcmSenderId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.messaging_sender_id();
}
const char* AppOptions_getDatabaseUrl(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.database_url();
}
const char* AppOptions_getGaTrackingId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.ga_tracking_id();
}
const char* AppOptions_getStorageBucket(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.storage_bucket();
}
const char* AppOptions_getProjectId(const NativeAppOptions* options) {
  if (!options) return nullptr;
  return options->delegate.project_id();
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
  auto* options = new NativeAppOptions();

  if (app_id) options->delegate.set_app_id(app_id);
  if (api_key) options->delegate.set_api_key(api_key);
  if (fcm_sender_id) options->delegate.set_messaging_sender_id(fcm_sender_id);
  if (database_url) options->delegate.set_database_url(database_url);
  if (ga_tracking_id) options->delegate.set_ga_tracking_id(ga_tracking_id);
  if (storage_bucket) options->delegate.set_storage_bucket(storage_bucket);
  if (project_id) options->delegate.set_project_id(project_id);

  return options;
}

NativeAppOptions* AppOptions_fromJson(const char* config) {
  if (!config || config[0] == '\0') return nullptr;
  const auto* result = AppOptions::LoadFromJsonConfig(config);
  if (!result) return nullptr;
  return new NativeAppOptions{*result};
}

void AppOptions_destroy(NativeAppOptions* options) {
  if (options) delete options;
}

// App getters
const char* App_getName(const NativeApp* app) {
  if (!app || !app->delegate) return nullptr;
  return app->delegate->name();
}
NativeAppOptions* App_getOptions(const NativeApp* app) {
  if (!app || !app->delegate) return nullptr;
  return new NativeAppOptions{app->delegate->options()};
}

// App methods
NativeApp* App_create(const NativeAppOptions* options, const char* name) {
  if (!options) return nullptr;
  App* app = (!name || name[0] == '\0') ? App::Create(options->delegate)
                                        : App::Create(options->delegate, name);
  if (!app) return nullptr;
  return new NativeApp{app};
}

NativeApp* App_getInstance(const char* name) {
  App* app = (!name || name[0] == '\0') ? App::GetInstance() : App::GetInstance(name);
  if (!app) return nullptr;
  return new NativeApp{app};
}

void App_destroy(NativeApp* app) {
  if (!app) return;
  if (app->delegate) delete app->delegate;
  delete app;
}

} // extern "C"
