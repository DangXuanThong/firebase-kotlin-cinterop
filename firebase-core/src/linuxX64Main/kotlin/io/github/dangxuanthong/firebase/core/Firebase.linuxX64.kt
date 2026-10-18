package io.github.dangxuanthong.firebase.core

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp.getInstance()

actual fun Firebase.app(name: String): FirebaseApp = FirebaseApp.getInstance(name)

actual fun Firebase.apps(context: Any?): List<FirebaseApp> = FirebaseApp.apps

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp =
    FirebaseApp.initializeApp(options)

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp =
    FirebaseApp.initializeApp(options, name)
