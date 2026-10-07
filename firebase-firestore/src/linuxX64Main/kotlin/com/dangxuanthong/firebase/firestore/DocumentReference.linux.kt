package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.firestore.exceptions.decodeFirestoreException
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
    actual suspend fun get(source: Source): DocumentSnapshot = suspendCancellableCoroutine { cont ->
        val ref = SafeRef.create(cont)
        val rc = fdb_fs_get(
            path,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val cont = userdata!!.asSafeRef<CancellableContinuation<DocumentSnapshot>>()
                    .also { it.disposeIfNotAlready() }
                    .get()
                val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                if (seq > 0) cont.resume(DocumentSnapshot(bytes)) { _, _, _ -> }
                else cont.resumeWithException(
                    decodeFirestoreException(bytes, "fdb_fs_get failed: seq=$seq")
                )
            }
        )
        if (rc < 0) {
            ref.disposeIfNotAlready()
            cont.resumeWithException(
                when (rc) {
                    -1L -> IllegalStateException("You must call Firebase.initialize first.")
                    else -> RuntimeException("Error during get: fdb_fs_get returned $rc")
                }
            )
        }
        cont.invokeOnCancellation { ref.disposeIfNotAlready() }
    }

    actual suspend fun <T : Any> set(
        strategy: SerializationStrategy<T>,
        data: T,
        setOptions: SetOptions
    ): Unit = suspendCancellableCoroutine { cont ->
        val cbor = Cbor.encodeToByteArray(strategy, data).toUByteArray()
        val ref = SafeRef.create(cont)
        val rc = fdb_fs_set(
            path,
            cbor.refTo(0),
            cbor.size.convert(),
            if (setOptions is SetOptions.Merge) 1 else 0,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val cont = userdata!!.asSafeRef<CancellableContinuation<Unit>>()
                    .also { it.disposeIfNotAlready() }
                    .get()
                if (seq > 0) cont.resume(Unit) { _, _, _ -> }
                else {
                    val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                    cont.resumeWithException(
                        decodeFirestoreException(bytes, "fdb_fs_set failed: seq=$seq")
                    )
                }
            }
        )
        if (rc < 0) {
            ref.disposeIfNotAlready()
            cont.resumeWithException(
                when (rc) {
                    -1L -> IllegalStateException("You must call Firebase.initialize first.")
                    -3L -> RuntimeException("Cannot serialize received document.")
                    else -> RuntimeException("Error during set: fdb_fs_set returned $rc")
                }
            )
        }
        cont.invokeOnCancellation { ref.disposeIfNotAlready() }
    }

    actual suspend fun delete(): Unit = TODO("Not yet implemented")
}
