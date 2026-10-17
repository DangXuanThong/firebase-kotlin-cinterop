package io.github.dangxuanthong.firebase.core

import com.google.firebase.Firebase as NativeFirebase
import com.google.firebase.FirebaseApp as NativeFirebaseApp
import com.google.firebase.app
import com.google.firebase.initialize
import io.github.dangxuanthong.firebase.core.utils.requireContext

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(NativeFirebase.app)

actual fun Firebase.app(name: String): FirebaseApp = FirebaseApp(NativeFirebase.app(name))

actual fun Firebase.apps(context: Any?): List<FirebaseApp> =
    NativeFirebaseApp.getApps(context.requireContext()).map { FirebaseApp(it) }

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp =
    FirebaseApp(NativeFirebase.initialize(context.requireContext(), options.toNative()))

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp =
    FirebaseApp(NativeFirebase.initialize(context.requireContext(), options.toNative(), name))
