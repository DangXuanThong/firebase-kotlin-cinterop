package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.core.Firebase

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

/** Returns the [FirebaseFirestore] instance of the default [FirebaseApp][com.dangxuanthong.firebase.core.FirebaseApp]. */
expect val Firebase.firestore: FirebaseFirestore
