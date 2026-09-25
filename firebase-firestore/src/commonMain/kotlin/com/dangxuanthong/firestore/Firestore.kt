package com.dangxuanthong.firestore

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

expect class FirebaseFirestore {
    fun collection(path: String): CollectionReference
    fun close()
}

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

expect class DocumentReference {
    val id: String
    suspend fun get(): DocumentSnapshot
}

expect class DocumentSnapshot {
    val exists: Boolean
    suspend fun <T> data(strategy: DeserializationStrategy<T>): T
}

suspend inline fun <reified T> DocumentSnapshot.data(): T = data(serializer())

data class FirestoreConfig(
    val applicationId: String,
    val apiKey: String,
    val databaseUrl: String?,
    val gaTrackingId: String?,
    val storageBucket: String?,
    val projectId: String?,
    val gcmSenderId: String?,
    val authDomain: String?
)

/**
 * Entry point for initializing Firebase, mirroring GitLive's own
 * `dev.gitlive.firebase.Firebase` — a bare anchor object; the real API
 * lives in extension functions declared on it.
 */
expect object Firebase

/**
 * Initializes Firebase for this process/app, returning a ready-to-use
 * [FirebaseFirestore].
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
expect fun Firebase.initialize(context: Any? = null, config: FirestoreConfig): FirebaseFirestore

class FirestoreConfigBuilder internal constructor() {
    var applicationId: String = ""
    var apiKey: String = ""
    var databaseUrl: String? = null
    var gaTrackingId: String? = null
    var storageBucket: String? = null
    var projectId: String? = null
    var gcmSenderId: String? = null
    var authDomain: String? = null
    internal fun build() = FirestoreConfig(
        applicationId,
        apiKey,
        databaseUrl,
        gaTrackingId,
        storageBucket,
        projectId,
        gcmSenderId,
        authDomain
    )
}

/** Builder-style convenience over [Firebase.initialize]; see its docs for [context]. */
fun Firebase.initialize(
    context: Any? = null,
    block: FirestoreConfigBuilder.() -> Unit
): FirebaseFirestore = initialize(context, FirestoreConfigBuilder().apply(block).build())

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
fun Firebase.initialize(path: String, context: Any? = null): FirebaseFirestore {
    val jsonText = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
    return initialize(context, parseGoogleServicesConfig(jsonText))
}
