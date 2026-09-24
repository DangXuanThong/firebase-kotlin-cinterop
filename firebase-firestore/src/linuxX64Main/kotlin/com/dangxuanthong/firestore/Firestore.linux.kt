package com.dangxuanthong.firestore

import fdb.fdb_app_init
import fdb.fdb_fs_get
import fdb.fdb_fs_init
import fdb.fdb_shutdown
import kotlin.coroutines.resumeWithException
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

actual class FirebaseFirestore {
    actual fun collection(path: String): CollectionReference = CollectionReference(path)
    actual fun close() {
        fdb_shutdown()
    }
}

actual class CollectionReference(private val path: String) {
    actual fun document(id: String): DocumentReference = DocumentReference("$path/$id")
}

actual class DocumentReference(private val path: String) {
    actual val id: String get() = path.substringAfterLast('/')

    actual suspend fun get(): DocumentSnapshot = suspendCancellableCoroutine { cont ->
        val ref = StableRef.create(cont)
        val rc = fdb_fs_get(
            path,
            ref.asCPointer(),
            staticCFunction { userdata, seq, payload, len ->
                val stable = userdata!!.asStableRef<CancellableContinuation<DocumentSnapshot>>()
                val c = stable.get()
                val bytes = if (len.toInt() > 0) payload?.readBytes(len.toInt()) else null
                stable.dispose()
                when (seq) {
                    1L -> c.resume(DocumentSnapshot(bytes)) { _, _, _ -> }

                    else -> {
                        val message = bytes?.let { Cbor.decodeFromByteArray<String>(it) }
                            ?: "fdb_fs_get failed: seq=$seq"
                        c.resumeWithException(RuntimeException(message))
                    }
                }
            }
        )
        if (rc != 0L) {
            ref.dispose()
            cont.resumeWithException(RuntimeException("fdb_fs_get returned $rc"))
        }
    }
}

actual class DocumentSnapshot(private val cbor: ByteArray?) {
    actual val exists: Boolean get() = cbor != null
    actual suspend fun <T> data(strategy: DeserializationStrategy<T>): T =
        Cbor.decodeFromByteArray(strategy, cbor ?: error("document does not exist"))
}

actual fun initializeFirestore(config: FirestoreConfig): FirebaseFirestore {
    val rc = fdb_app_init(config.appId, config.apiKey, config.projectId, "", "")
    check(rc == 0L) { "fdb_app_init failed: $rc" }
    fdb_fs_init()
    return FirebaseFirestore()
}
