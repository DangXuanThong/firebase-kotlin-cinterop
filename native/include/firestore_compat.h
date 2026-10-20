#ifndef FIRESTORE_COMPAT_H
#define FIRESTORE_COMPAT_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include "firebase_compat.h"

#ifdef __cplusplus
namespace firebase {
namespace firestore {
class Firestore;
class DocumentReference;
class CollectionReference;
class Settings;
}
}
typedef firebase::firestore::Firestore NativeFirestore;
typedef firebase::firestore::DocumentReference NativeDocRef;
typedef firebase::firestore::CollectionReference NativeColRef;
typedef firebase::firestore::Settings NativeSettings;
extern "C" {
#else
typedef struct NativeFirestore NativeFirestore;
typedef struct NativeDocRef NativeDocRef;
typedef struct NativeDocSnapshot NativeDocSnapshot;
typedef struct NativeColRef NativeColRef;
typedef struct NativeListenerRegistration NativeListenerRegistration;
typedef struct NativeSettings NativeSettings;
#endif

// Snapshot Listener Callbacks (for real-time snapshots)
typedef void (*SnapshotSuccessCallback)(
  void* user_data,
  const uint8_t* payload,
  size_t payload_len
);
typedef void (*SnapshotFailureCallback)(
  void* user_data,
  int error_code,
  const char* error_message
);

typedef SnapshotSuccessCallback DocSnapshotSuccessCallback;
typedef SnapshotFailureCallback DocSnapshotFailureCallback;

// --- Firestore ---
NativeFirestore* Firestore_getInstance(NativeApp* app, const char* database);
NativeColRef* Firestore_collection(NativeFirestore* firestore, const char* collection_path);
NativeDocRef* Firestore_document(NativeFirestore* firestore, const char* document_path);
void Firestore_setSettings(NativeFirestore* firestore, NativeSettings* settings);
void Firestore_terminate(
  NativeFirestore* firestore,
  void* user_data,
  FutureSuccessCallback on_success,
  FutureFailureCallback on_failure
);
void Firestore_destroy(NativeFirestore* firestore);

// --- Settings ---
NativeSettings* Settings_create(
  const char* host,
  bool ssl_enabled,
  bool persistence_enabled,
  int64_t cache_size_bytes
);
void Settings_destroy(NativeSettings* settings);

// --- CollectionReference ---
void ColRef_destroy(NativeColRef* col_ref);

// --- DocumentReference ---
void DocRef_destroy(NativeDocRef* doc_ref);

#ifdef __cplusplus
}
#endif

#endif // FIRESTORE_COMPAT_H
