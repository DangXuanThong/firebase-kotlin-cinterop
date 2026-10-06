package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.FieldPath`
 *
 * A `FieldPath` refers to a field in a document. The path may consist of a single field name
 * (referring to a top level field in the document), or a list of field names (referring to a nested
 * field in the document).
 */
class FieldPath(private val segments: List<String>) {

    init {
        require(segments.isNotEmpty()) { "Invalid field path. Provided path must not be empty." }
        segments.forEachIndexed { i, seg ->
            require(seg.isNotEmpty()) {
                "Invalid field name at argument ${i + 1}. Field names must not be empty."
            }
        }
    }

    /**
     * Creates a [FieldPath] from the provided field names. If more than one field name is
     * provided, the path will point to a nested field in a document.
     *
     * @param fieldNames A list of field names.
     * @return A [FieldPath] that points to a field location in a document.
     */
    constructor(vararg fieldNames: String) : this(fieldNames.toList())

    val encoded: String
        get() = segments.joinToString(".")

    override fun equals(other: Any?) = other is FieldPath && segments == other.segments
    override fun hashCode() = segments.hashCode()
    override fun toString() = segments.joinToString(".")

    companion object {
        /**
         * A special sentinel [FieldPath] to refer to the ID of a document. It can be used
         * in queries to sort or filter by the document ID.
         */
        val documentId: FieldPath = FieldPath(KEY_PATH)
        internal const val KEY_PATH = "__name__"
    }
}
