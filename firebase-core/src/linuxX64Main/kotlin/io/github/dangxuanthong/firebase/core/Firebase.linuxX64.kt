package io.github.dangxuanthong.firebase.core

import io.github.dangxuanthong.firebase.core.native.App_create
import io.github.dangxuanthong.firebase.core.native.App_getInstance
import kotlinx.cinterop.cstr

actual val Firebase.app: FirebaseApp
    get() = App_getInstance(null)?.let { FirebaseApp(it) }
        ?: throw IllegalStateException(
            "Default Firebase is not initialized in this process. " +
                "Make sure to call Firebase.initialize(...) first."
        )

actual fun Firebase.app(name: String): FirebaseApp = App_getInstance(name)?.let { FirebaseApp(it) }
    ?: throw IllegalStateException(
//        "FirebaseApp with name $name doesn't exist. Available: ${apps().map { it.name }}"
        "FirebaseApp with name $name doesn't exist."
    )

actual fun Firebase.apps(context: Any?): List<FirebaseApp> = TODO("Will be implemented in phase 4")

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp {
    check(App_getInstance(null) == null) {
        "Default FirebaseApp already exists!"
    }
    return FirebaseApp(App_create(options = options.toNative(), name = null)!!)
}

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    val normalizedName = name.trim()
    check(App_getInstance(normalizedName.cstr) == null) {
        "FirebaseApp name $normalizedName already exists!"
    }
    return FirebaseApp(App_create(options = options.toNative(), name = normalizedName.cstr)!!)
}
