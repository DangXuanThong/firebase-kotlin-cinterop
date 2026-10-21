package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.firestore.CollectionReference as NativeCollectionReference

actual class CollectionReference internal constructor(
    actual val firestore: FirebaseFirestore,
    private val delegate: NativeCollectionReference
) {

    actual val id: String
        get() = delegate.id
    actual val path: String
        get() = delegate.path
}
