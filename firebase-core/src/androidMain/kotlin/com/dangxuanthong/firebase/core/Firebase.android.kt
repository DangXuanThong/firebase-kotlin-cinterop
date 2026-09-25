package com.dangxuanthong.firebase.core

import android.content.Context
import dev.gitlive.firebase.Firebase as RealFirebase
import dev.gitlive.firebase.FirebaseOptions as RealFirebaseOptions
import dev.gitlive.firebase.app
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.initialize

actual object Firebase

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(RealFirebase.app)

actual fun Firebase.app(name: String) = FirebaseApp(RealFirebase.app(name))

actual fun Firebase.apps(context: Any?) = RealFirebase.apps(context).map { FirebaseApp(it) }

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp {
    check(context is Context) { "Android requires a real Context to initialize Firestore" }
    RealFirebase.apps(context).firstOrNull()
        ?: RealFirebase.initialize(context, options.toRealFirebaseOptions())
    return FirebaseApp(RealFirebase.app)
}

actual fun Firebase.initialize(
    context: Any?,
    options: FirebaseOptions,
    name: String
): FirebaseApp {
    check(context is Context) { "Android requires a real Context to initialize Firestore" }
    RealFirebase.apps(context).firstOrNull { it.name == name }
        ?: RealFirebase.initialize(context, options.toRealFirebaseOptions(), name)
    return FirebaseApp(RealFirebase.app(name))
}

private fun FirebaseOptions.toRealFirebaseOptions() = RealFirebaseOptions(
    applicationId = applicationId,
    apiKey = apiKey,
    databaseUrl = databaseUrl,
    projectId = projectId,
    storageBucket = storageBucket,
    gaTrackingId = gaTrackingId,
    gcmSenderId = gcmSenderId
)
