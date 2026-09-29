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

    /**
     * Clears [app] if it's still the registered instance. Returns whether *this* call did
     * the clearing, so a caller can tell "I deleted it" from "someone already had".
     */
    fun clear(app: FirebaseApp): Boolean = ref.compareAndSet(app, null)
}
