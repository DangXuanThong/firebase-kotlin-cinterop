package io.github.dangxuanthong.firebase.firestore

/**
 * Mirroring `com.google.firebase.firestore.CollectionReference`
 *
 * A [CollectionReference] can be used for adding documents, getting document references, and
 * querying for documents (using the methods inherited from [Query]).
 *
 * **Subclassing Note**: Cloud Firestore classes are not meant to be subclassed except for use
 * in test mocks. Subclassing is not supported in production code and new SDK releases may break
 * code that does so.
 */
expect class CollectionReference /*: Query*/ {

    /** The ID of the collection. */
    val id: String

    /**
     * The path of this collection (relative to the root of the database) as a slash-separated
     * string.
     */
    val path: String

    /** The Cloud Firestore instance associated with this query. */
    val firestore: FirebaseFirestore

//    val parent: DocumentReference?

//    fun document(path: String? = null): DocumentReference
//    suspend fun <T : Any> add(data: T): DocumentReference
}
