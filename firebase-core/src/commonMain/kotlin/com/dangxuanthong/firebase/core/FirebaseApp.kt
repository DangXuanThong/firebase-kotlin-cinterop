package com.dangxuanthong.firebase.core

/**
 * The entry point of Firebase SDKs. It holds common configuration and state for Firebase APIs. Most
 * applications don't need to directly interact with FirebaseApp.
 *
 * In the event that an app requires access to another Firebase project <strong>in addition
 * to</strong> the default project, {@link FirebaseApp#initializeApp(Context, FirebaseOptions,
 * String)} must be used to create that relationship programmatically. The name parameter must be
 * unique. To connect to the resources exposed by that project, use the {@link FirebaseApp} object
 * returned by {@link FirebaseApp#getInstance(String)}, passing it the same name used with <code>
 * initializeApp</code>. This object must be passed to the static accessor of the feature that
 * provides the resource. For example, {@link
 * com.google.firebase.storage.FirebaseStorage#getInstance(FirebaseApp)getInstance(FirebaseApp)} is
 * used to access the storage bucket provided by the additional project, whereas {@link
 * com.google.firebase.storage.FirebaseStorage#getInstance()} is used to access the default project.
 *
 * Any <code>FirebaseApp</code> initialization must occur only in the main process of the app.
 * Use of Firebase in processes other than the main process is not supported and will likely cause
 * problems related to resource contention.
 *
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
