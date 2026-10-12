package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.firestore.mappers.toRealServerTimestampBehavior
import com.dangxuanthong.firebase.firestore.mappers.toSnapshotMetadata
import dev.gitlive.firebase.firestore.DocumentSnapshot as RealDocumentSnapshot
import kotlinx.serialization.DeserializationStrategy

actual class DocumentSnapshot(private val delegate: RealDocumentSnapshot) {
    actual val id: String
        get() = delegate.id
    actual val exists: Boolean
        get() = delegate.exists
    actual val reference: DocumentReference
        get() = DocumentReference(delegate.reference)
    actual val metadata: SnapshotMetadata
        get() = delegate.metadata.toSnapshotMetadata()

    actual fun <T> data(
        strategy: DeserializationStrategy<T>,
        serverTimestampBehavior: ServerTimestampBehavior
    ): T = delegate.data(strategy, serverTimestampBehavior.toRealServerTimestampBehavior())
}
