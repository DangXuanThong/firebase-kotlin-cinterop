package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeFirestore
import io.github.dangxuanthong.firebase.core.FirebaseApp
import io.github.dangxuanthong.firebase.core.native.Firestore_destroy
import io.github.dangxuanthong.firebase.core.native.Firestore_getDefaultDatabase
import io.github.dangxuanthong.firebase.core.native.Firestore_getInstance
import io.github.dangxuanthong.firebase.core.native.Firestore_getSettings
import io.github.dangxuanthong.firebase.core.native.Firestore_setSettings
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

actual class FirebaseFirestore private constructor(
    private val firebaseApp: FirebaseApp,
    private val database: String,
    delegate: CPointer<NativeFirestore>
) : AutoCloseable {

    private var delegate: CPointer<NativeFirestore>? = delegate

    actual var settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings()
        get() = Firestore_getSettings(delegate)!!.use { FirebaseFirestoreSettings.fromNative(it) }
        set(value) {
            value.toNative().use { Firestore_setSettings(delegate, it) }
            field = value
        }

//    actual fun collection(collectionPath: String): CollectionReference =
//        CollectionReference(collectionPath)
//
//    actual fun document(documentPath: String): DocumentReference =
//        DocumentReference(documentPath)


    override fun close() = lock.withLock {
        Firestore_destroy(delegate)
        instances.remove(FirestoreKey(firebaseApp, database))
        delegate = null
    }

    actual companion object {
        actual val DEFAULT_DATABASE: String
            get() = Firestore_getDefaultDatabase()!!.toKString()

        private val instances = mutableMapOf<FirestoreKey, FirebaseFirestore>()
        private val lock = reentrantLock()

        actual fun getInstance(
            app: FirebaseApp,
            database: String
        ): FirebaseFirestore = lock.withLock {
            checkNotNull(app.delegate) { "FirebaseApp was deleted" }
            val key = FirestoreKey(app, database)
            if (instances.containsKey(key)) return@withLock instances[key]!!

            val nativeFirestore = Firestore_getInstance(app.delegate, database)
                ?: throw IllegalStateException("Failed to initialize native Firestore instance")
            instances[key] = FirebaseFirestore(app, database, nativeFirestore)
            instances[key]!!
        }

        private data class FirestoreKey(val app: FirebaseApp, val database: String)
    }
}
