package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeFirestore
import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp
import io.github.dangxuanthong.firebase.core.app
import io.github.dangxuanthong.firebase.core.native.Firestore_destroy
import io.github.dangxuanthong.firebase.core.native.Firestore_getInstance
import io.github.dangxuanthong.firebase.core.native.Firestore_setSettings
import io.github.dangxuanthong.firebase.core.native.getDefaultDatabaseNames
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

actual class FirebaseFirestore internal constructor(
    private val firebaseApp: FirebaseApp,
    private val databaseId: String,
    delegate: CPointer<NativeFirestore>,
    settings: FirebaseFirestoreSettings
) : AutoCloseable {

    private var delegate: CPointer<NativeFirestore>? = delegate

    actual var settings: FirebaseFirestoreSettings = settings
        @Deprecated("Property can only be written.", level = DeprecationLevel.ERROR)
        get() = throw NotImplementedError()
        set(value) {
            Firestore_setSettings(delegate, value.toNative())
            field
        }

//    actual fun collection(collectionPath: String): CollectionReference =
//        CollectionReference(collectionPath)
//
//    actual fun document(documentPath: String): DocumentReference =
//        DocumentReference(documentPath)


    override fun close() = synchronized(lock) {
        Firestore_destroy(delegate)
        instances.remove(FirestoreKey(firebaseApp, databaseId))
        delegate = null
    }

    internal companion object {
        private val instances = mutableMapOf<FirestoreKey, FirebaseFirestore>()
        private val lock = SynchronizedObject()

        fun getInstance(
            app: FirebaseApp = Firebase.app,
            databaseId: String = getDefaultDatabaseNames()!!.toKString(),
            settings: FirebaseFirestoreSettings
        ): FirebaseFirestore = synchronized(lock) {
            checkNotNull(app.delegate) { "FirebaseApp was deleted" }
            val key = FirestoreKey(app, databaseId)
            if (instances.containsKey(key)) return@synchronized instances[key]!!

            settings.toNative().use {
                val nativeFirestore = Firestore_getInstance(app.delegate, databaseId)
                    ?: throw IllegalStateException("Failed to initialize native Firestore instance")
                Firestore_setSettings(nativeFirestore, it)
                instances[key] = FirebaseFirestore(app, databaseId, nativeFirestore, settings)
                instances[key]!!
            }
        }

        private data class FirestoreKey(val app: FirebaseApp, val databaseId: String)
    }
}
