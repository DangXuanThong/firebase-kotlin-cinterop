package com.dangxuanthong.firebase.firestore

import fdb.fdb_fs_get
import fdb.fdb_fs_listen
import fdb.fdb_fs_set
import fdb.fdb_fs_unlisten
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.resumeWithException
import kotlinx.cinterop.convert
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
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
                close(RuntimeException("fdb_fs_listen returned $listenerId"))
                return@callbackFlow
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
    actual suspend fun get(source: Source): DocumentSnapshot = suspendCancellableCoroutine { cont ->
        val ref = SafeRef.create(cont)
        val rc = fdb_fs_get(
            path,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val ref = userdata!!.asSafeRef<CancellableContinuation<DocumentSnapshot>>()
                val cont = ref.get()
                val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                ref.disposeIfNotAlready()
                if (seq == 1L) cont.resume(DocumentSnapshot(bytes)) { _, _, _ -> }
                else cont.resumeWithException(
                    decodeFirestoreException(bytes, "fdb_fs_get failed: seq=$seq")
                )
            }
        )
        if (rc != 0L) {
            ref.disposeIfNotAlready()
            cont.resumeWithException(RuntimeException("fdb_fs_get returned $rc"))
        }
        cont.invokeOnCancellation { ref.disposeIfNotAlready() }
    }

    actual suspend fun <T : Any> set(
        strategy: SerializationStrategy<T>,
        data: T,
        setOptions: SetOptions
    ) = suspendCancellableCoroutine<Unit> { cont ->
        val cbor = Cbor.encodeToByteArray(strategy, data).toUByteArray()
        val ref = SafeRef.create(cont)
        val rc = fdb_fs_set(
            path,
            cbor.refTo(0),
            cbor.size.convert(),
            if (setOptions is SetOptions.Merge) 1 else 0,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val ref = userdata!!.asSafeRef<CancellableContinuation<Unit>>()
                val cont = ref.get()
                ref.disposeIfNotAlready()
                if (seq == 1L) cont.resume(Unit) { _, _, _ -> }
                else {
                    val bytes = payload?.readBytes(len.toInt())?.takeIf {
                        it.isNotEmpty()
                    }
                    cont.resumeWithException(
                        decodeFirestoreException(
                            bytes,
                            "fdb_fs_set failed: seq=$seq"
                        )
                    )
                }
            }
        )

        if (rc != 0L) {
            ref.disposeIfNotAlready()
            cont.resumeWithException(RuntimeException("fdb_fs_set returned $rc"))
            return@suspendCancellableCoroutine
        }
        cont.invokeOnCancellation { ref.disposeIfNotAlready() }
    }

    actual suspend fun delete(): Unit = TODO("Not yet implemented")
}
