package com.dangxuanthong.firebase.firestore

import kotlinx.coroutines.flow.Flow

/**
 * Mirroring `dev.gitlive.firebase.firestore.DocumentReference`
 *
 * A `DocumentReference` refers to a document location in a Cloud Firestore database and can
 * be used to write, read, or listen to the location. There may or may not exist a document at the
 * referenced location. A `DocumentReference` can also be used to create a
 * [CollectionReference] to a subcollection.
 *
 * **Subclassing Note**: Cloud Firestore classes are not meant to be subclassed except for use
 * in test mocks. Subclassing is not supported in production code and new SDK releases may break
 * code that does so.
 */
expect class DocumentReference {

    /** Returns the ID for this document (that is, the last path segment). */
    val id: String

    /**
     * The path of this document (relative to the root of the database) as a slash-separated string.
     */
    val path: String

    /**
     * Starts listening to the document referenced by this `DocumentReference` with the given options
     * and emits its values via a [Flow].
     */
    val snapshots: Flow<DocumentSnapshot>

    /**
     * The [CollectionReference] to the collection that contains this document.
     */
    val parent: CollectionReference

    /**
     * Starts listening to the document referenced by this `DocumentReference` with the given options
     * and emits its values via a [Flow].
     *
     * @param includeMetadataChanges controls metadata-only changes. Default: `false`
     */
    fun snapshots(includeMetadataChanges: Boolean = false): Flow<DocumentSnapshot>

    /**
     * Gets a [CollectionReference] instance that refers to the subcollection at the specified
     * path relative to this document.
     *
     * @param collectionPath A slash-separated relative path to a subcollection.
     * @return The [CollectionReference] instance.
     */
    fun collection(collectionPath: String): CollectionReference

    /**
     * Reads the document referenced by this [DocumentReference].
     *
     * By default, `get()` attempts to provide up-to-date data when possible by waiting for
     * data from the server, but it may return cached data or fail if you are offline and the server
     * cannot be reached. This behavior can be altered via the [Source] parameter.
     *
     * @param source A value to configure the get behavior.
     * @return A Task that will be resolved with the contents of the Document at this [DocumentReference].
     */
    suspend fun get(source: Source = Source.DEFAULT): DocumentSnapshot

//    suspend inline fun <reified T : Any> set(
//        data: T,
//        merge: Boolean = false,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <reified T : Any> set(
//        data: T,
//        vararg mergeFields: String,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <reified T : Any> set(
//        data: T,
//        vararg mergeFieldPaths: FieldPath,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <T : Any> set(
//        strategy: SerializationStrategy<T>,
//        data: T,
//        merge: Boolean = false,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <T : Any> set(
//        strategy: SerializationStrategy<T>,
//        data: T,
//        vararg mergeFields: String,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <T : Any> set(
//        strategy: SerializationStrategy<T>,
//        data: T,
//        vararg mergeFieldPaths: FieldPath,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <reified T : Any> update(
//        data: T,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )
//
//    suspend inline fun <T : Any> update(
//        strategy: SerializationStrategy<T>,
//        data: T,
//        buildSettings: EncodeSettings.Builder.() -> Unit = {}
//    )

//    /**
//     * Updates Fields/[FieldPath] using a [FieldsAndValuesUpdateDSL].
//     * @param fieldsAndValuesUpdateDSL closure for configuring the [FieldsAndValuesUpdateDSL]
//     */
//    suspend fun updateFields(
//        fieldsAndValuesUpdateDSL: FieldsAndValuesUpdateDSL.() -> Unit
//    )

    suspend fun delete()
}
