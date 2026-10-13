package io.github.dangxuanthong.firebase.firestore

import io.github.dangxuanthong.firebase.core.Firebase

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

/** Returns the [FirebaseFirestore] instance of the default [FirebaseApp][io.github.dangxuanthong.firebase.core.FirebaseApp]. */
expect val Firebase.firestore: FirebaseFirestore
