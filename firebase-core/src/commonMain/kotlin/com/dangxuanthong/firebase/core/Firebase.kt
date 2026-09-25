package com.dangxuanthong.firebase.core

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

/**
 * Single access point to all firebase sdks from Kotlin.
 *
 * Acts as a target for extension methods provided by sdks.
 */
expect object Firebase

/** Returns the default firebase app instance. */
expect val Firebase.app: FirebaseApp

/** Returns a named firebase app instance. */
expect fun Firebase.app(name: String): FirebaseApp

/** Returns all firebase app instances. */
expect fun Firebase.apps(context: Any? = null): List<FirebaseApp>

/**
 * Initializes and returns a FirebaseApp.
 *
 * [context] is platform-dependent and ignored where not needed:
 * - **Android**: required — pass a real `android.content.Context`
 *   (e.g. `applicationContext`, or the instrumentation target context in
 *   tests). Firebase cannot initialize without one.
 * - **JVM / native**: ignored, pass `null`.
 *
 * Safe to call more than once with the same config — later calls are a
 * no-op on every platform (mirrors the underlying SDK's own behavior).
 */
expect fun Firebase.initialize(context: Any? = null, options: FirebaseOptions): FirebaseApp

/** Initializes and returns a FirebaseApp. */
expect fun Firebase.initialize(
    context: Any? = null,
    options: FirebaseOptions,
    name: String
): FirebaseApp

/** Builder-style convenience over [Firebase.initialize]; see its docs for [context]. */
fun Firebase.initialize(
    context: Any? = null,
    block: FirestoreConfigBuilder.() -> Unit
): FirebaseApp = initialize(context, FirestoreConfigBuilder().apply(block).build())

/**
 * Reads a `google-services.json`-shaped file from [path] and initializes
 * Firebase from it.
 *
 * **Not usable on Android.** Files bundled with an Android app are packaged
 * as APK assets, not real filesystem entries — [FileSystem.Companion.SYSTEM] has no
 * visibility into them at all. On Android, read the file yourself via
 * `Context.assets.open(...)` instead, then call
 * `Firebase.initialize(parseGoogleServicesConfig(jsonText), context)`
 * directly; or copy `google-service.json` to `assets` directory
 *
 * **On platforms where this does work (JVM, native desktop), [path]
 * resolution is caller-dependent, not library-dependent.** A relative
 * [okio.Path] resolves against the calling *process's* current working
 * directory at the moment this runs — a property of the OS process as a
 * whole, unrelated to which module or source file this function happens
 * to be declared in. That cwd varies by how the process was launched
 * (`./gradlew run...` vs. running the compiled binary directly vs. an
 * IDE run configuration can all differ) — pass an absolute path if you
 * need this to behave the same way regardless of invocation method.
 */
fun Firebase.initialize(context: Any? = null, path: String): FirebaseApp {
    val jsonText = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
    return initialize(context, parseGoogleServicesConfig(jsonText))
}
