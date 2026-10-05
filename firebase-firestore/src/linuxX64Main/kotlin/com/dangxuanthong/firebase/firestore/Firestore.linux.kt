package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.firestore.exceptions.FirestoreException
import fdb.fdb_fs_init
import fdb.fdb_shutdown
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

actual class FirebaseFirestore {
    init {
        fdb_fs_init()
    }

    actual fun collection(path: String): CollectionReference =
        CollectionReference(path)

    actual fun close() {
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

@Serializable
data class ErrorPayload(val code: Int, val message: String)

internal fun decodeFirestoreException(bytes: ByteArray?, fallback: String): FirestoreException {
    if (bytes == null) {
        return FirestoreException.Unknown(fallback)
    }
    return try {
        val payload = Cbor.decodeFromByteArray<ErrorPayload>(bytes)
        FirestoreException.fromCode(payload.code, payload.message)
    } catch (_: Exception) {
        FirestoreException.Unknown(fallback)
    }
}
