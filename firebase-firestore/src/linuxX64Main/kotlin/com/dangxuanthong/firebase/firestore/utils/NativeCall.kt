package com.dangxuanthong.firebase.firestore.utils

import com.dangxuanthong.firebase.firestore.exceptions.decodeFirestoreException
import fdb.FdbCallback
import kotlin.coroutines.resumeWithException
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun <T : Any> suspendNativeCall(
    nativeFunction: (COpaquePointer, FdbCallback) -> Long,
    config: NativeCallConfig<T>.() -> Unit
): T = suspendCancellableCoroutine { cont ->
    val nativeCallConfig = NativeCallConfig(cont).apply(config)
    require(nativeCallConfig.check()) { "onSuccess must be called inside NativeCallScope block" }

    val ref = StableRef.create(nativeCallConfig)
    val rc = nativeFunction(
        ref.asCPointer(),
        staticCFunction { userdata, seq, payload, len ->
            userdata!!.useAsStableRef<NativeCallConfig<T>> { (cont, onSuccess, onFailure) ->
                val bytes = payload?.readBytes(len.toInt())?.takeIf { it.isNotEmpty() }
                cont.resumeWith(
                    runCatching {
                        if (seq > 0) onSuccess(bytes)
                        else throw onFailure(bytes, seq)
                    }
                )
            }
        }
    )
    if (rc < 0L) {
        ref.dispose()
        cont.resumeWithException(nativeCallConfig.error(rc))
    }
}

@Suppress("ktlint:standard:statement-wrapping")
class NativeCallConfig<T> internal constructor(
    internal val continuation: CancellableContinuation<T>
) {
    internal lateinit var success: (ByteArray?) -> T
        private set
    internal var failure: (ByteArray?, Long) -> Throwable = { byte, _ ->
        decodeFirestoreException(byte)
    }
        private set
    internal var error: (Long) -> Throwable = {
        when (it) {
            -1L -> IllegalStateException("You must call Firebase.initialize first.")
            else -> RuntimeException("Operation failed with unknown code: $it.")
        }
    }
        private set

    fun onSuccess(block: (ByteArray?) -> T) { success = block }
    fun onFailure(block: (ByteArray?, Long) -> Throwable) { failure = block }
    fun onError(block: (Long) -> Throwable) { error = block }

    internal fun check() = ::success.isInitialized

    internal operator fun component1() = continuation
    internal operator fun component2() = success
    internal operator fun component3() = failure
}

internal inline fun <reified T : Any> COpaquePointer.useAsStableRef(block: (T) -> Unit) =
    this.asStableRef<T>().let {
        try {
            block(it.get())
        } finally {
            it.dispose()
        }
    }
