package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeFirestore
import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp
import io.github.dangxuanthong.firebase.core.app
import io.github.dangxuanthong.firebase.core.native.Firestore_destroy
import io.github.dangxuanthong.firebase.core.native.Firestore_getInstance
import io.github.dangxuanthong.firebase.core.native.Firestore_setSettings
import io.github.dangxuanthong.firebase.core.native.getDefaultDatabaseNames
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

actual class FirebaseFirestore internal constructor(
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

    internal companion object {
        private val instances = mutableMapOf<FirestoreKey, FirebaseFirestore>()
        private val lock = reentrantLock()

        fun getInstance(
            app: FirebaseApp = Firebase.app,
            database: String = getDefaultDatabaseNames()!!.toKString()
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
