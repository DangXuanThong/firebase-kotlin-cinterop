package com.dangxuanthong.firebase.core

import fdb.fdb_shutdown

actual class FirebaseApp internal constructor(
    actual val name: String,
    actual val options: FirebaseOptions
) {
    actual suspend fun delete() {
        fdb_shutdown()
    }
}
