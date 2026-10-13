package io.github.dangxuanthong.firebase.firestore

import fdb.fdb_fs_delete
import fdb.fdb_fs_get
import fdb.fdb_fs_listen
import fdb.fdb_fs_set
import fdb.fdb_fs_unlisten
import io.github.dangxuanthong.firebase.firestore.utils.flowNativeCall
import io.github.dangxuanthong.firebase.firestore.utils.suspendNativeCall
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.cbor.Cbor

@ConsistentCopyVisibility
actual data class DocumentReference internal constructor(actual val path: String) {

    actual val id: String
        get() = path.substringAfterLast('/')

    actual val snapshots: Flow<DocumentSnapshot>
        get() = flowNativeCall(
            nativeFunction = { userdata, callback ->
                fdb_fs_listen(doc_path = path, userdata, callback)
            }
        ) {
            onSuccess {
                DocumentSnapshot(
                    id = id,
                    reference = this@DocumentReference,
                    metadata = SnapshotMetadata(hasPendingWrites = false, isFromCache = false),
                    cborBytes = it
                )
            }
            onCleanup { fdb_fs_unlisten(listener_id = it) }
        }

    actual val parent: CollectionReference
        get() = CollectionReference(path.substringBeforeLast('/'))

    actual fun snapshots(includeMetadataChanges: Boolean): Flow<DocumentSnapshot> =
        TODO("Not yet implemented")

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference("$path/$collectionPath")

    // Keep Source here since I think I'll implement caching in future
    actual suspend fun get(source: Source): DocumentSnapshot = suspendNativeCall(
        nativeFunction = { userdata, callback -> fdb_fs_get(doc_path = path, userdata, callback) }
    ) {
        onSuccess {
            DocumentSnapshot(
                id = id,
                reference = this@DocumentReference,
                metadata = SnapshotMetadata(hasPendingWrites = false, isFromCache = false),
                cborBytes = it
            )
        }
    }

    actual suspend fun <T : Any> set(
        strategy: SerializationStrategy<T>,
        data: T,
        setOptions: SetOptions
    ): Unit = suspendNativeCall(
        nativeFunction = { userdata, callback ->
            val cbor = Cbor.encodeToByteArray(strategy, data).toUByteArray()
            fdb_fs_set(
                doc_path = path,
                cbor = cbor.refTo(0),
                len = cbor.size.convert(),
                merge = if (setOptions is SetOptions.Merge) 1 else 0,
                userdata,
                callback
            )
        }
    ) {
        onError {
            when (it) {
                -1L -> IllegalStateException("You must call Firebase.initialize first.")
                -3L -> RuntimeException("Cannot serialize received document.")
                else -> RuntimeException("Operation failed with unknown code: $it.")
            }
        }

        onSuccess { }
    }

    actual suspend fun delete(): Unit = suspendNativeCall(
        nativeFunction = { userdata, callback ->
            fdb_fs_delete(doc_path = path, userdata, callback)
        }
    ) { onSuccess { } }
}
