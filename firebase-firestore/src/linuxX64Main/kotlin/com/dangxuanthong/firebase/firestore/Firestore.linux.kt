package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.core.Firebase

actual class CollectionReference(private val path: String) {
    actual fun document(id: String): DocumentReference =
        DocumentReference("$path/$id")
}

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(firestoreSettings {})
