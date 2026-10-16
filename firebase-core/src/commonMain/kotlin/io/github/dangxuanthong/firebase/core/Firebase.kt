package io.github.dangxuanthong.firebase.core

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

    /**
     * Options of the default [FirebaseApp].
     *
     * @throws IllegalStateException if the default app was not initialized.
     */
    val options: FirebaseOptions
        get() = Firebase.app.options

    /** Builder-style convenience over [initialize][io.github.dangxuanthong.firebase.core.initialize]. */
    fun initialize(
        context: Any? = null,
        name: String? = null,
        block: FirebaseOptionsBuilder.() -> Unit
    ): FirebaseApp {
        val options = FirebaseOptionsBuilder().apply(block).build()
        // Use default app if name is null
        return if (name == null) initialize(context, options)
        else initialize(context, options, name)
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
 * Initializes the default [FirebaseApp] instance.
 *
 * @param context platform-dependent and ignored where not needed:
 *  - Android: required - pass a real `android.content.Context`
 *          (e.g. `applicationContext`, or the instrumentation target context in tests).
 *          Firebase cannot initialize without one.
 *  - Others: ignored, pass `null`.
 * @param options represents the global [FirebaseOptions]
 * @return the default [FirebaseApp] if it has been initialized previously or a new one.
 */
expect fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp

/**
 * A factory method to initialize a [FirebaseApp].
 *
 * @param context platform-dependent and ignored where not needed:
 *  - Android: required - pass a real `android.content.Context`
 *          (e.g. `applicationContext`, or the instrumentation target context in tests).
 *          Firebase cannot initialize without one.
 *  - Others: ignored, pass `null`.
 * @param options represents the global [FirebaseOptions]
 * @param name unique name for the app. It is an error to initialize an app with an already
 * existing name.
 * @return an instance of [FirebaseApp]
 * @throws IllegalStateException if an app with the same name has already been initialized.
 */
expect fun Firebase.initialize(
    context: Any? = null,
    options: FirebaseOptions,
    name: String
): FirebaseApp
