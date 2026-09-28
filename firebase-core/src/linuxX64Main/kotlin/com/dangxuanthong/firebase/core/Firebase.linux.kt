@file:OptIn(ExperimentalAtomicApi::class)

package com.dangxuanthong.firebase.core

import com.dangxuanthong.firebase.core.exceptions.FirebaseException
import fdb.fdb_app_init
import fdb.fdb_fs_init
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.updateAndFetch

actual val Firebase.app: FirebaseApp
    get() = registeredApp.load()
        ?: error(
            "Default FirebaseApp is not initialized." +
                " Make sure to call Firebase.initialize() first."
        )

actual fun Firebase.app(name: String): FirebaseApp {
    require(name == DEFAULT_APP_NAME) {
        "Firebase on Linux only support 1 app named: $DEFAULT_APP_NAME"
    }
    return app
}

actual fun Firebase.apps(context: Any?) = listOfNotNull(registeredApp.load())

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    require(name == DEFAULT_APP_NAME) {
        "Firebase on Linux only support 1 app named: $DEFAULT_APP_NAME"
    }
    return registeredApp.updateAndFetch { registeredApp ->
        if (registeredApp != null) return registeredApp

        val rc = fdb_app_init(
            options.applicationId,
            options.apiKey,
            options.projectId,
            options.databaseUrl,
            options.storageBucket
        )
        if (rc != 0L) throw FirebaseException("fdb_app_init failed: $rc")
        fdb_fs_init() // TODO: Move this fdb_fd_init() into firebase-firestore module
        return@updateAndFetch FirebaseApp(name, options)
    }!!
}

private var registeredApp: AtomicReference<FirebaseApp?> = AtomicReference(null)
