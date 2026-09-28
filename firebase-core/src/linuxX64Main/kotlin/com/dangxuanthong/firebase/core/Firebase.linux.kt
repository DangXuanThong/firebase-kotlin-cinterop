package com.dangxuanthong.firebase.core

import com.dangxuanthong.firebase.core.exceptions.FirebaseException
import fdb.fdb_app_init
import fdb.fdb_fs_init

actual val Firebase.app: FirebaseApp
    get() = AppRegistry.current
        ?: error(
            "Default FirebaseApp is not initialized." +
                " Make sure to call Firebase.initialize() first."
        )

actual fun Firebase.app(name: String): FirebaseApp {
    check(name == DEFAULT_APP_NAME) {
        "FirebaseApp with name $name doesn't exist. " +
            "Firebase on Linux supports a single app named $DEFAULT_APP_NAME."
    }
    return app
}

actual fun Firebase.apps(context: Any?): List<FirebaseApp> = listOfNotNull(AppRegistry.current)

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    require(name == DEFAULT_APP_NAME) {
        "Firebase on Linux supports a single app named $DEFAULT_APP_NAME"
    }
    AppRegistry.current?.let { return it }

    val rc = fdb_app_init(
        options.applicationId,
        options.apiKey,
        options.projectId,
        options.databaseUrl,
        options.storageBucket
    )
    if (rc != 0L) throw FirebaseException("fdb_app_init failed: $rc")
    fdb_fs_init() // TODO: move into the firebase-firestore module

    return AppRegistry.publish(FirebaseApp(name, options))
}
