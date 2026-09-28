@file:OptIn(ExperimentalAtomicApi::class)

package com.dangxuanthong.firebase.core

import fdb.fdb_shutdown
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

actual class FirebaseApp internal constructor(
    actual val name: String,
    actual val options: FirebaseOptions
) {
    private val deleted = AtomicBoolean(false)

    actual suspend fun delete() {
        if (!deleted.compareAndSet(false, true)) return // no-op if already deleted
        AppRegistry.clear(this) // unregister first, so nobody gets this app mid-shutdown
        fdb_shutdown()
    }

    override fun toString() = "FirebaseApp(name=$name)"
}
