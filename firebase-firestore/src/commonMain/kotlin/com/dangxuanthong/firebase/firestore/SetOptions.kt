package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.internal.SetOptions`
 *
 * An options object that configures the behavior of `set()` calls. By providing one of the
 * SetOptions objects returned by [Merge], [MergeFields], [MergeFieldPaths] and [Overwrite],
 * the `set()` calls in [DocumentReference], [WriteBatch] and [Transaction] can be configured
 * to perform granular merges instead of overwriting the target documents in their entirety.
 */
sealed interface SetOptions {

    /**
     * Changes the behavior of `set()` calls to only replace the values specified in its data
     * argument. Fields omitted from the `set()` call will remain untouched. If your input sets
     * any field to an empty map, all nested fields are overwritten.
     */
    data object Merge : SetOptions

    data object Overwrite : SetOptions

    /**
     * Changes the behavior of `set()` calls to only replace the given fields. Any field that is
     * not specified in [fields] is ignored and remains untouched. If your input sets any field
     * to an empty map, all nested fields are overwritten.
     *
     * It is an error to pass a [SetOptions] object to a `set()` call that is missing a
     * value for any of the fields specified here.
     *
     * @param fields The list of fields to merge. Fields can contain dots to reference nested fields
     *     within the document.
     */
    data class MergeFields(val fields: List<String>) : SetOptions

    /**
     * Changes the behavior of `set()` calls to only replace the given fields. Any field that is
     * not specified in [fieldPaths] is ignored and remains untouched.
     *
     * It is an error to pass a [SetOptions] object to a `set()` call that is missing a
     * value for any of the fields specified here.
     *
     * @param fieldPaths The list of fields to merge.
     */
    data class MergeFieldPaths(val fieldPaths: List<FieldPath>) : SetOptions {
        val encodedFieldPaths = fieldPaths.map { it.encoded }
    }
}
