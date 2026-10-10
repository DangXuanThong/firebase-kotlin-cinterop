package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.firestore.exceptions.decodeFirestoreException
import com.dangxuanthong.firebase.firestore.utils.suspendNativeCall
import com.dangxuanthong.firebase.firestore.utils.useAsStableRef
import fdb.fdb_fs_delete
import fdb.fdb_fs_get
import fdb.fdb_fs_listen
import fdb.fdb_fs_set
import fdb.fdb_fs_unlisten
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.convert
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.cbor.Cbor

@OptIn(ExperimentalAtomicApi::class)
@ConsistentCopyVisibility
actual data class DocumentReference internal constructor(actual val path: String) {

    actual val id: String
        get() = path.substringAfterLast('/')

    actual val snapshots: Flow<DocumentSnapshot>
        get() = callbackFlow {
            val ref = SafeRef.create(this)
            val listenerId = fdb_fs_listen(
                path,
                ref.asCPointer(),
                staticCFunction { userdata, seq, payload, len ->
                    val producer = userdata!!.asSafeRef<ProducerScope<DocumentSnapshot>>().get()
                    val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                    if (seq > 0) producer.trySend(DocumentSnapshot(bytes))
                    else producer.close(
                        decodeFirestoreException(bytes, "listener failed: seq=$seq")
                    )
                }
            )
            if (listenerId < 0) {
                ref.disposeIfNotAlready()
                close(
                    when (listenerId) {
                        -1L -> IllegalStateException("You must call Firebase.initialize first.")

                        else -> RuntimeException(
                            "Error during listen: fdb_fs_listen returned $listenerId"
                        )
                    }
                )
            }

            awaitClose {
                fdb_fs_unlisten(listenerId)
                ref.disposeIfNotAlready()
            }
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
    ) { onSuccess { DocumentSnapshot(it) } }

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
