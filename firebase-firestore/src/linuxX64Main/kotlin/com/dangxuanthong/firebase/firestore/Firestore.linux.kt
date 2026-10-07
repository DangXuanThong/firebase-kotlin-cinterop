package com.dangxuanthong.firebase.firestore

import fdb.fdb_fs_init
import fdb.fdb_fs_shutdown
import fdb.fdb_shutdown
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor

actual class FirebaseFirestore {
    init {
        fdb_fs_init()
    }

    actual fun collection(path: String): CollectionReference =
        CollectionReference(path)

    actual fun close() {
        fdb_fs_shutdown()
        fdb_shutdown()
    }
}

actual class CollectionReference(private val path: String) {
    actual fun document(id: String): DocumentReference =
        DocumentReference("$path/$id")
}

actual class DocumentSnapshot(private val cbor: ByteArray?) {
    actual val exists: Boolean get() = cbor != null
    actual suspend fun <T> data(strategy: DeserializationStrategy<T>): T =
        Cbor.decodeFromByteArray(strategy, cbor ?: error("document does not exist"))
}
