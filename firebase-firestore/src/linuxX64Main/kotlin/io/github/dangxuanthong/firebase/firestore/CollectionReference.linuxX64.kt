package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeColRef
import io.github.dangxuanthong.firebase.core.native.ColRef_destroy
import kotlinx.cinterop.CPointer

actual class CollectionReference internal constructor(
    actual val path: String,
    actual val firestore: FirebaseFirestore,
    private val delegate: CPointer<NativeColRef>
) : AutoCloseable {

    actual val id: String
        get() = path.split("/").last()

    init {
        // Check of com.google.firebase.firestore.model.ResourcePath.fromString
        require(!path.contains("//")) { "Invalid path ($path). Paths must not contain // in them." }
        // Check of com.google.firebase.firestore.CollectionReference.<init>
        require(path.split("/").size % 2 == 1) {
            "Invalid collection reference. Collection references must have an odd number " +
                "of segments, but $path has ${path.length}"
        }
    }

    override fun close() {
        ColRef_destroy(delegate)
    }
}
