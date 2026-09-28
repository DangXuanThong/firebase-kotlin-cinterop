@file:OptIn(ExperimentalAtomicApi::class)

package com.dangxuanthong.firebase.core

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

internal object AppRegistry {
    private val ref = AtomicReference<FirebaseApp?>(null)

    val current: FirebaseApp?
        get() = ref.load()

    /** Publishes [app] unless another thread already did; returns whichever instance won. */
    fun publish(app: FirebaseApp): FirebaseApp =
        if (ref.compareAndSet(null, app)) app else checkNotNull(ref.load())

    /** Clears the registry only if [app] is still the registered instance. */
    fun clear(app: FirebaseApp) {
        ref.compareAndSet(app, null)
    }
}
