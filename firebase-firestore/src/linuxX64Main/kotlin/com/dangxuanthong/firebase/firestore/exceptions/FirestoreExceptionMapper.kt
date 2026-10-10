package com.dangxuanthong.firebase.firestore.exceptions

import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

/**
 * Represents the CBOR-encoded error payload structure returned from the native C-interop layer.
 *
 * @property code The numeric error code corresponding to a Firestore status code.
 * @property message The human-readable error description returned by the native library.
 */
@Serializable
private data class ErrorPayload(val code: Int, val message: String)

/**
 * Decodes a CBOR-encoded error byte array received from the native layer into a [FirestoreException].
 *
 * - If `this` is `null`, returns [FirestoreException.Unknown] indicating no error payload was provided.
 * - Decodes CBOR bytes into [ErrorPayload] and converts its status code using [FirestoreException.fromCode].
 * - If decoding fails (e.g., malformed payload), returns [FirestoreException.Internal] wrapping the exception.
 *
 * @return The mapped [FirestoreException] instance.
 */
internal fun ByteArray?.asFirestoreException(): FirestoreException {
    if (this == null) return FirestoreException.Unknown("No error payload received")
    return try {
        val payload = Cbor.decodeFromByteArray<ErrorPayload>(this)
        FirestoreException.fromCode(payload.code, payload.message)
    } catch (e: IllegalArgumentException) {
        FirestoreException.Internal(e.message ?: "Could not decode error payload", e)
    }
}
