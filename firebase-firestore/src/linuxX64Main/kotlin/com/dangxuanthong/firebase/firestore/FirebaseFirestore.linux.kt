package com.dangxuanthong.firebase.firestore

import fdb.fdb_fs_shutdown

actual class FirebaseFirestore(actual var settings: FirebaseFirestoreSettings) {

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(collectionPath)

    actual fun document(documentPath: String): DocumentReference =
        DocumentReference(documentPath)

    actual suspend fun terminate() {
        fdb_fs_shutdown()
    }
}
