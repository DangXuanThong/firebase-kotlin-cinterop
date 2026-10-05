package com.dangxuanthong.firebase.firestore

import fdb.fdb_fs_get
import fdb.fdb_fs_listen
import fdb.fdb_fs_unlisten
import kotlin.coroutines.resumeWithException
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

@ConsistentCopyVisibility
actual data class DocumentReference internal constructor(actual val path: String) {

    actual val id: String
        get() = path.substringAfterLast('/')

    actual val snapshots: Flow<DocumentSnapshot>
        get() = callbackFlow {
            val ref = StableRef.create(this)
            val listenerId = fdb_fs_listen(
                path,
                ref.asCPointer(),
                staticCFunction { userdata, seq, payload, len ->
                    val ref = userdata!!.asStableRef<ProducerScope<DocumentSnapshot>>().get()
                    val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                    if (seq > 0) ref.trySend(DocumentSnapshot(bytes))
                    else ref.close(decodeFirestoreException(bytes, "listener failed: seq=$seq"))
                }
            )
            if (listenerId < 0) {
                ref.dispose()
                close(RuntimeException("fdb_fs_listen returned $listenerId"))
                return@callbackFlow
            }

            awaitClose {
                fdb_fs_unlisten(listenerId)
                ref.dispose()
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
        val ref = StableRef.create(cont)
        val rc = fdb_fs_get(
            path,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val ref = userdata!!.asStableRef<CancellableContinuation<DocumentSnapshot>>()
                val cont = ref.get()
                val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                ref.dispose()
                if (seq == 1L) cont.resume(DocumentSnapshot(bytes)) { _, _, _ -> }
                else cont.resumeWithException(
                    decodeFirestoreException(bytes, "fdb_fs_get failed: seq=$seq")
                )
            }
        )
        if (rc != 0L) {
            ref.dispose()
            cont.resumeWithException(RuntimeException("fdb_fs_get returned $rc"))
        }
    }

    actual suspend fun delete(): Unit = TODO("Not yet implemented")
}
