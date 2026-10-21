package io.github.dangxuanthong.firebase.firestore

import io.github.dangxuanthong.firebase.core.Firebase

actual class CollectionReference(private val path: String) {
    actual fun document(id: String): DocumentReference =
        DocumentReference("$path/$id")
}

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()

actual fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore =
    FirebaseFirestore.getInstance(app)

actual fun Firebase.firestore(app: FirebaseApp, database: String): FirebaseFirestore =
    FirebaseFirestore.getInstance(app, database)

actual fun Firebase.firestore(database: String): FirebaseFirestore =
    FirebaseFirestore.getInstance(database = database)
