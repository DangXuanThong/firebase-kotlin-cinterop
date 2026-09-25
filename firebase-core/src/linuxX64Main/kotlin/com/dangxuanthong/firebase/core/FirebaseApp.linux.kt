package com.dangxuanthong.firebase.core

import fdb.fdb_shutdown

// Default app name from gitlive's sdk, firebase_ffi only support 1 app at a time so we use
// this name directly
internal const val DEFAULT_APP_NAME = "[DEFAULT]"

actual class FirebaseApp internal constructor(actual val options: FirebaseOptions) {
    actual val name: String
        get() = DEFAULT_APP_NAME

    actual suspend fun delete() {
        fdb_shutdown()
    }
}
