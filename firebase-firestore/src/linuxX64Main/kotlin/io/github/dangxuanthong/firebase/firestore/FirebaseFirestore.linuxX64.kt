package io.github.dangxuanthong.firebase.firestore

import fdb.fdb_fs_init
import fdb.fdb_fs_shutdown

actual class FirebaseFirestore(actual var settings: FirebaseFirestoreSettings) {
    init {
        fdb_fs_init()
    }

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(collectionPath)

    actual fun document(documentPath: String): DocumentReference =
        DocumentReference(documentPath)

    actual suspend fun terminate() {
        fdb_fs_shutdown()
    }
}
