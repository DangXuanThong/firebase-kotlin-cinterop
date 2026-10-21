package io.github.dangxuanthong.firebase.firestore

import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

/** Returns the [FirebaseFirestore] instance of the default [FirebaseApp][io.github.dangxuanthong.firebase.core.FirebaseApp]. */
val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()

/** Returns the [FirebaseFirestore] instance of a given [FirebaseApp]. */
fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore = FirebaseFirestore.getInstance(app)

/** Returns the [FirebaseFirestore] instance of a given [FirebaseApp] and database name. */
fun Firebase.firestore(app: FirebaseApp, database: String): FirebaseFirestore =
    FirebaseFirestore.getInstance(app, database)

/**
 * Returns the [FirebaseFirestore] instance of the default [FirebaseApp], given the database name.
 */
fun Firebase.firestore(database: String): FirebaseFirestore =
    FirebaseFirestore.getInstance(database = database)
