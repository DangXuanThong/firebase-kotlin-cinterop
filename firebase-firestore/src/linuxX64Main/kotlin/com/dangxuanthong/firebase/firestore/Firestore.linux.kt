package com.dangxuanthong.firebase.firestore

actual class CollectionReference(private val path: String) {
    actual fun document(id: String): DocumentReference =
        DocumentReference("$path/$id")
}
