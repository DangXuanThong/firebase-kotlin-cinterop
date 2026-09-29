package com.dangxuanthong.firebase.core

import fdb.fdb_shutdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

actual class FirebaseApp internal constructor(
    private val internalName: String,
    private val internalOptions: FirebaseOptions
) {
    actual val name: String
        get() {
            check(AppRegistry.current === this) { "FirebaseApp \"$internalName\" was deleted" }
            return internalName
        }

    actual val options: FirebaseOptions
        get() {
            check(AppRegistry.current === this) { "FirebaseApp \"$internalName\" was deleted" }
            return internalOptions
        }

    actual suspend fun delete() {
        if (!AppRegistry.clear(this)) return
        withContext(Dispatchers.IO) {
            fdb_shutdown()
        }
    }

    override fun toString() = "FirebaseApp(name=$internalName)"
}
