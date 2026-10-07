package com.dangxuanthong.firebase.firestore.exceptions

import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

@Serializable
private data class ErrorPayload(val code: Int, val message: String)

@PublishedApi
internal fun decodeFirestoreException(bytes: ByteArray?, fallback: String): FirestoreException {
    if (bytes == null) return FirestoreException.Unknown(fallback)
    return try {
        val payload = Cbor.decodeFromByteArray<ErrorPayload>(bytes)
        FirestoreException.fromCode(payload.code, payload.message)
    } catch (_: IllegalArgumentException) {
        FirestoreException.Unknown(fallback)
    }
}
