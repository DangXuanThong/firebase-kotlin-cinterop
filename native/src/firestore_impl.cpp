#include <string>
#include <cstring>

#include "firebase/app.h"
#include "firebase/firestore.h"

#include "firestore_compat.h"
#include "future_bridge.hpp"

using namespace firebase::firestore;

extern "C" {

// --- Firestore Instance ---
NativeFirestore* Firestore_getInstance(NativeApp* app, const char* database) {
  if (!database || database[0] == '\0')
    return app ? Firestore::GetInstance(app) : Firestore::GetInstance();
  return app ? Firestore::GetInstance(app, database) : Firestore::GetInstance(database);
}

NativeColRef* Firestore_collection(NativeFirestore* firestore, const char* collection_path) {
  if (!firestore || !collection_path) return nullptr;
  return new CollectionReference(firestore->Collection(collection_path));
}

NativeDocRef* Firestore_document(NativeFirestore* firestore, const char* document_path) {
  if (!firestore || !document_path) return nullptr;
  return new DocumentReference(firestore->Document(document_path));
}

void Firestore_setSettings(NativeFirestore* firestore, NativeSettings* settings) {
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
void ColRef_destroy(NativeColRef* col_ref) {
  delete col_ref;
}

// --- Document Reference ---
void DocRef_destroy(NativeDocRef* doc_ref) {
  delete doc_ref;
}

} // extern "C"
