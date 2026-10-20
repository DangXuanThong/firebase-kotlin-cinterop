package io.github.dangxuanthong.firebase.firestore

expect class FirebaseFirestore {
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
}
