package com.dangxuanthong.firebase.core

/**
 * Mirroring `dev.gitlive.firebase.FirebaseApp`
 *
 * The entry point of Firebase SDKs. It holds common configuration and state for Firebase APIs. Most
 * applications don't need to directly interact with FirebaseApp.
 *
 * Any `FirebaseApp` initialization must occur only in the main process of the app.
 * Use of Firebase in processes other than the main process is not supported and will likely cause
 * problems related to resource contention.
 */
expect class FirebaseApp {
    /** Returns the unique name of this app. */
    val name: String

    /** Returns the specified [FirebaseOptions]. */
    val options: FirebaseOptions

    /**
     * Deletes the [FirebaseApp] and all its data. All calls to this [FirebaseApp]
     * instance will throw once it has been called.
     *
     * A no-op if delete was called before.
     */
    suspend fun delete()
}
