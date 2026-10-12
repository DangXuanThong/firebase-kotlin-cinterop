package com.dangxuanthong.firebase.firestore

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor

actual data class DocumentSnapshot(
    actual val id: String,
    actual val reference: DocumentReference,
    actual val metadata: SnapshotMetadata,
    private val cborBytes: ByteArray?
) {
    actual val exists: Boolean
        get() = cborBytes != null && cborBytes.isNotEmpty()

    actual fun <T> data(
        strategy: DeserializationStrategy<T>,
        serverTimestampBehavior: ServerTimestampBehavior
    ): T = cborBytes?.let { Cbor.decodeFromByteArray(strategy, it) }
        ?: throw RuntimeException("Could not deserialize null document.")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DocumentSnapshot

        if (exists != other.exists) return false
        if (id != other.id) return false
        if (reference != other.reference) return false
        if (metadata != other.metadata) return false
        if (!cborBytes.contentEquals(other.cborBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = exists.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + reference.hashCode()
        result = 31 * result + metadata.hashCode()
        result = 31 * result + (cborBytes?.contentHashCode() ?: 0)
        return result
    }
}
