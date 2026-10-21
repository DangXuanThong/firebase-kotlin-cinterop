#include <string>
#include <cstring>

#include "firebase/app.h"
#include "firebase/firestore.h"

#include "firestore_compat.h"
#include "future_bridge.hpp"

using namespace firebase::firestore;

extern "C" {

// --- Firestore Instance ---
const char* Firestore_getDefaultDatabase() {
  return firebase::firestore::kDefaultDatabase;
}

NativeFirestore* Firestore_getInstance(NativeApp* app, const char* database) {
  if (!database || database[0] == '\0')
    return app ? Firestore::GetInstance(app) : Firestore::GetInstance();
  return app ? Firestore::GetInstance(app, database) : Firestore::GetInstance(database);
}

NativeSettings* Firestore_getSettings(const NativeFirestore* firestore) {
  if (!firestore) return nullptr;
  return new Settings(firestore->settings());
}

NativeColRef* Firestore_collection(const NativeFirestore* firestore, const char* collection_path) {
  if (!firestore || !collection_path) return nullptr;
  return new CollectionReference(firestore->Collection(collection_path));
}

NativeDocRef* Firestore_document(const NativeFirestore* firestore, const char* document_path) {
  if (!firestore || !document_path) return nullptr;
  return new DocumentReference(firestore->Document(document_path));
}

void Firestore_setSettings(NativeFirestore* firestore, const NativeSettings* settings) {
  if (!firestore || !settings) return;
  firestore->set_settings(*settings);
}

void Firestore_terminate(
    NativeFirestore* firestore,
    void* user_data,
    FutureSuccessCallback on_success,
    FutureFailureCallback on_failure
) {
  if (!firestore) {
    if (on_failure) on_failure(user_data, -1, "Invalid Firestore pointer");
    return;
  }
  const auto future = firestore->Terminate();
  future_bridge::Future_bindCallback(future, on_success, on_failure, user_data);
}

void Firestore_destroy(NativeFirestore* firestore) {
  delete firestore;
}

// --- Firestore Settings ---
const char* Settings_getHost(const NativeSettings* settings) {
  if (!settings) return nullptr;
  thread_local std::string tls_host;
  tls_host = settings->host();
  return tls_host.c_str();
}
bool Settings_isSslEnabled(const NativeSettings* settings) {
  return settings && settings->is_ssl_enabled();
}
bool Settings_isPersistenceEnabled(const NativeSettings* settings) {
  return settings && settings->is_persistence_enabled();
}
int64_t Settings_getCacheSizeBytes(const NativeSettings* settings) {
  return settings ? settings->cache_size_bytes() : -1;
}

NativeSettings* Settings_create(
    const char* host,
    bool ssl_enabled,
    bool persistence_enabled,
    int64_t cache_size_bytes
) {
  auto* settings = new Settings();
  if (host && host[0] != '\0') settings->set_host(host);
  settings->set_ssl_enabled(ssl_enabled);
  settings->set_persistence_enabled(persistence_enabled);
  settings->set_cache_size_bytes(cache_size_bytes);
  return settings;
}

void Settings_destroy(NativeSettings* settings) {
  delete settings;
}

// --- Collection Reference ---
const char* ColRef_getPath(const NativeColRef* col_ref) {
  if (!col_ref) return nullptr;
  thread_local std::string tls_col_path;
  tls_col_path = col_ref->path();
  return tls_col_path.c_str();
}

void ColRef_destroy(NativeColRef* col_ref) {
  delete col_ref;
}

// --- Document Reference ---
void DocRef_destroy(NativeDocRef* doc_ref) {
  delete doc_ref;
}

} // extern "C"
