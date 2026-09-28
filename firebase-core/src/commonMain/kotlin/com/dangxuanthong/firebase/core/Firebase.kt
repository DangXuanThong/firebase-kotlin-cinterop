package com.dangxuanthong.firebase.core

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

/**
 * Mirroring `dev.gitlive.firebase.Firebase`
 *
 * Single access point to all firebase sdks from Kotlin.
 *
 * Acts as a target for extension methods provided by sdks.
 */
object Firebase {
    val DEFAULT_APP_NAME: String
        get() = "[DEFAULT]"

    val options: FirebaseOptions
        get() = Firebase.app.options

    /**
     * Initializes the default [FirebaseApp] instance, uses [DEFAULT_APP_NAME] as name.
     */
    fun initialize(context: Any?, options: FirebaseOptions) =
        initialize(context, options, DEFAULT_APP_NAME)

    /** Builder-style convenience over [Firebase.initialize] */
    fun initialize(
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
    fun initialize(context: Any? = null, path: String): FirebaseApp {
        val jsonText = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
        return initialize(context, parseGoogleServicesConfig(jsonText))
    }
}

/**
 * Returns the default (first initialized) instance of the [FirebaseApp].
 *
 * @throws IllegalStateException if the default app was not initialized.
 */
expect val Firebase.app: FirebaseApp

/**
 * Returns the instance identified by the unique name, or throws if it does not exist.
 *
 * @param name represents the name of the [FirebaseApp] instance.
 * @throws IllegalStateException if the [FirebaseApp] was not initialized
 */
expect fun Firebase.app(name: String): FirebaseApp

/** Returns list of all FirebaseApps. */
expect fun Firebase.apps(context: Any? = null): List<FirebaseApp>

/**
 * Initializes a [FirebaseApp] instance.
 *
 * @param context platform-dependent and ignored where not needed:
 *  - Android: required - pass a real `android.content.Context`
 *          (e.g. `applicationContext`, or the instrumentation target context in tests).
 *          Firebase cannot initialize without one.
 *  - Others: ignored, pass `null`.
 * @param options represents the global [FirebaseOptions]
 * @param name unique name for the app. It is an error to initialize an app with an already
 *  existing name. Starting and ending whitespace characters in the name are ignored (trimmed).
 */
expect fun Firebase.initialize(
    context: Any? = null,
    options: FirebaseOptions,
    name: String
): FirebaseApp
