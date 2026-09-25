package com.dangxuanthong.firebase.core

import fdb.fdb_app_init
import fdb.fdb_fs_init

actual object Firebase

actual val Firebase.app: FirebaseApp
    get() = registeredApp
        ?: error("Firebase app not initialized, init it using Firebase.initialize()")

actual fun Firebase.app(name: String): FirebaseApp {
    require(name == DEFAULT_APP_NAME) {
        "Firebase on Linux only support 1 app named: $DEFAULT_APP_NAME"
    }
    return app
}

actual fun Firebase.apps(context: Any?) = listOfNotNull(registeredApp)

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp {
    val rc = fdb_app_init(
        options.applicationId,
        options.apiKey,
        options.projectId,
        options.databaseUrl,
        options.storageBucket
    )
    check(rc == 0L) { "fdb_app_init failed: $rc" }
    fdb_fs_init()
    return FirebaseApp(options)
}

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    require(name == DEFAULT_APP_NAME) {
        "Firebase on Linux only support 1 app named: $DEFAULT_APP_NAME"
    }
    return initialize(context, options)
}

private var registeredApp: FirebaseApp? = null
