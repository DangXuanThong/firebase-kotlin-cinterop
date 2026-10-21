package io.github.dangxuanthong.firebase.firestore

import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp
import io.github.dangxuanthong.firebase.core.app

/**
 * Mirroring `com.google.firebase.firestore.FirebaseFirestore`
 *
 * Represents a Cloud Firestore database and is the entry point for all Cloud Firestore operations.
 *
 * **Subclassing Note**: Cloud Firestore classes are not meant to be subclassed except for use
 * in test mocks. Subclassing is not supported in production code and new SDK releases may break
 * code that does so.
 */
expect class FirebaseFirestore {
    /** The settings used by this [FirebaseFirestore] object. */
    var settings: FirebaseFirestoreSettings

//    fun collection(collectionPath: String): CollectionReference

//    fun collectionGroup(collectionId: String): Query = Query(wrapper.collectionGroup(collectionId))
//    fun document(documentPath: String): DocumentReference

//    fun batch(): WriteBatch
//    fun setLoggingEnabled(loggingEnabled: Boolean) {}
//    suspend fun clearPersistence()
//    suspend fun <T> runTransaction(block: suspend Transaction.() -> T): T
//    fun useEmulator(host: String, port: Int)
//    suspend fun disableNetwork()
//    suspend fun enableNetwork()
//    suspend fun terminate()
//    suspend fun waitForPendingWrites()

    companion object {
        val DEFAULT_DATABASE: String

        /**
         * Returns the [FirebaseFirestore] instance for the provided [FirebaseApp].
         *
         * Returns the same instance for all invocations given the same [FirebaseApp] and
         * database parameter. If no instance exists, initializes a new instance.
         *
         * @param app The [FirebaseApp] instance that the returned [FirebaseFirestore]
         *     instance is associated with.
         * @param database The database ID.
         * @returns The [FirebaseFirestore] instance.
         */
        fun getInstance(
            app: FirebaseApp = Firebase.app,
            database: String = DEFAULT_DATABASE
        ): FirebaseFirestore
    }
}
