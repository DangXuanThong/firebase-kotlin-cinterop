package com.dangxuanthong.firebase.firestore

import dev.gitlive.firebase.firestore.DocumentReference as RealDocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class DocumentReference internal constructor(private val delegate: RealDocumentReference) {

    actual val id: String
        get() = delegate.id
    actual val path: String
        get() = delegate.path
    actual val snapshots: Flow<DocumentSnapshot>
        get() = delegate.snapshots.map { DocumentSnapshot(it) }
    actual val parent: CollectionReference
        get() = CollectionReference(delegate.parent)

    actual fun snapshots(includeMetadataChanges: Boolean): Flow<DocumentSnapshot> =
        delegate.snapshots(includeMetadataChanges).map { DocumentSnapshot(it) }

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(delegate.collection(collectionPath))

    actual suspend fun get(source: Source): DocumentSnapshot =
        DocumentSnapshot(delegate.get(source.toRealSource()))

    actual suspend fun delete() = delegate.delete()

    override fun equals(other: Any?): Boolean =
        other is DocumentReference && delegate == other.delegate

    override fun hashCode(): Int = delegate.hashCode()
    override fun toString(): String = delegate.toString()
}
