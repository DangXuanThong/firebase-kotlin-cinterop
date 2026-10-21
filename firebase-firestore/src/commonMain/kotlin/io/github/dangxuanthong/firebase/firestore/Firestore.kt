package io.github.dangxuanthong.firebase.firestore

import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

/** Returns the [FirebaseFirestore] instance of the default [FirebaseApp][io.github.dangxuanthong.firebase.core.FirebaseApp]. */
expect val Firebase.firestore: FirebaseFirestore

/** Returns the [FirebaseFirestore] instance of a given [FirebaseApp]. */
expect fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore

/** Returns the [FirebaseFirestore] instance of a given [FirebaseApp] and database name. */
expect fun Firebase.firestore(app: FirebaseApp, database: String): FirebaseFirestore

/**
 * Returns the [FirebaseFirestore] instance of the default [FirebaseApp], given the database name.
 */
expect fun Firebase.firestore(database: String): FirebaseFirestore
