package com.dangxuanthong.firebase.firestore

import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef

@ExperimentalAtomicApi
@ExperimentalForeignApi
class SafeRef<T : Any> @PublishedApi internal constructor(private val ref: StableRef<T>) {

    private val isDisposed = AtomicBoolean(false)

    fun asCPointer(): COpaquePointer = ref.asCPointer()

    fun get(): T = ref.get()

    /** Disposes if nothing has yet — ordinary synchronous call, safe from anywhere, callback included. */
    fun disposeIfNotAlready() {
        if (isDisposed.compareAndSet(expectedValue = false, newValue = true)) ref.dispose()
    }

    companion object {
        fun <T : Any> create(value: T): SafeRef<T> = SafeRef(StableRef.create(value))
    }
}

@ExperimentalAtomicApi
@ExperimentalForeignApi
inline fun <reified T : Any> COpaquePointer.asSafeRef(): SafeRef<T> = SafeRef(asStableRef())
