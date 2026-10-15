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

NativeAppOptions* appOptions_create(const char* app_id, const char* api_key,
    const char* fcm_sender_id, const char* database_url, const char* ga_tracking_id,
    const char* storage_bucket, const char* project_id) {

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

NativeAppOptions* appOptions_from_json(const char* config) {
  if (!config) return nullptr;
  auto* options = new NativeAppOptions();
  if (!AppOptions::LoadFromJsonConfig(config, &options->delegate)) {
    delete options;
    return nullptr;
  }
  return options;
}

void appOptions_destroy(NativeAppOptions* options) {
  if (options) delete options;
}

} // extern "C"
