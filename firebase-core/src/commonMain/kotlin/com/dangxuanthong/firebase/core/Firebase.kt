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
    const val DEFAULT_APP_NAME = "[DEFAULT]"

    /**
     * Options of the default [FirebaseApp].
     *
     * @throws IllegalStateException if the default app was not initialized.
     */
    val options: FirebaseOptions
        get() = Firebase.app.options

    /**
     * Initializes the default [FirebaseApp] instance, uses [DEFAULT_APP_NAME] as name.
     *
     * See the platform-level `initialize(context, options, name)` for how [context] is used
     * and what happens when the app already exists.
     */
    fun initialize(context: Any?, options: FirebaseOptions): FirebaseApp =
        initialize(context, options, DEFAULT_APP_NAME)

    /** Builder-style convenience over [initialize]. */
    fun initialize(
        context: Any? = null,
        block: FirestoreConfigBuilder.() -> Unit
    ): FirebaseApp = initialize(context, FirestoreConfigBuilder().apply(block).build())

    /**
     * Reads a `google-services.json`-shaped file from [path] and initializes
     * the default [FirebaseApp] from it. Because [context] comes first, call it with a
     * named argument: `Firebase.initialize(path = "google-services.json")`.
     *
     * **Not usable directly on Android.** Files bundled with an Android app are packaged
     * as APK assets, not real filesystem entries, and [FileSystem.SYSTEM] has no visibility
     * into them. On Android, either read the file via `Context.assets.open(...)` and call
     * `Firebase.initialize(context, parseGoogleServicesConfig(jsonText))`, or copy it out of
     * `assets` into `context.filesDir` first and pass that absolute path here.
     *
     * **Where this works (JVM, native desktop), a relative [path] is resolved by the caller's
     * environment, not by the library.** It resolves against the *process's* current working
     * directory at the moment this runs, which is unrelated to the module or source file this
     * function is declared in. That directory varies with how the process was launched
     * (`./gradlew run...`, running the compiled binary directly, and an IDE run configuration
     * can all differ), so pass an absolute path if you need the same behavior regardless of
     * invocation method.
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
 * Initializes the [FirebaseApp] named [name], or returns the existing instance if an app with
 * that name is already initialized. In the latter case [options] are ignored, so the returned
 * app keeps the options it was first created with. Safe to call more than once.
 *
 * @param context platform-dependent and ignored where not needed:
 *  - Android: required - pass a real `android.content.Context`
 *          (e.g. `applicationContext`, or the instrumentation target context in tests).
 *          Firebase cannot initialize without one.
 *  - Others: ignored, pass `null`.
 * @param options represents the global [FirebaseOptions]
 * @param name unique name for the app. Linux supports only [Firebase.DEFAULT_APP_NAME].
 */
expect fun Firebase.initialize(
    context: Any? = null,
    options: FirebaseOptions,
    name: String
): FirebaseApp
