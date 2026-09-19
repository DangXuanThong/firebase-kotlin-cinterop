@file:OptIn(ExperimentalAtomicApi::class)

import fdb.fdb_app_init
import fdb.fdb_fs_get
import fdb.fdb_fs_init
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.staticCFunction
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import platform.posix.usleep

@Serializable
data class TestDoc(val message: String)

class Result {
    val done = AtomicInt(0)
    var seq: Long = 0
    var bytes: ByteArray? = null
}

@OptIn(
    ExperimentalSerializationApi::class,
    ExperimentalForeignApi::class,
    ExperimentalForeignApi::class
)
fun main() {
    val appRc = fdb_app_init(
        "...",
        "...",
        "...",
        "", // database_url — not using Realtime Database
        "" // storage_bucket — not using Storage
    )
    if (appRc != 0L) {
        println("fdb_app_init failed: $appRc")
        return
    }

    fdb_fs_init()

    val result = Result()
    val ref = StableRef.create(result)

    val rc = fdb_fs_get(
        "cinterop_test/hello",
        ref.asCPointer(),
        staticCFunction { userdata, seq, payload, len ->
            val r = userdata!!.asStableRef<Result>().get()
            r.bytes = payload?.readBytes(len.toInt())
            r.seq = seq
            r.done.store(1)
        }
    )

    if (rc != 0L) {
        println("fdb_fs_get returned error $rc")
        ref.dispose()
        return
    }

    while (result.done.load() == 0) {
        usleep(50_000u)
    }
    ref.dispose()

    println("seq=${result.seq}, bytes=${result.bytes?.size ?: 0}")
    result.bytes?.let { bytes ->
        val doc = Cbor.decodeFromByteArray<TestDoc>(bytes)
        println("decoded: $doc")
        println("message = ${doc.message}")
    }
}
