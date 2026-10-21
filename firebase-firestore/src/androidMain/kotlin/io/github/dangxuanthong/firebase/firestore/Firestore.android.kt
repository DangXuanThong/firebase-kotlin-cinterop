package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.Firebase as NativeFirebase
import com.google.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.FirebaseApp

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) =
        DocumentReference(delegate.document(id))
}

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(NativeFirebase.firestore)

actual fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore =
    FirebaseFirestore(NativeFirebase.firestore(app.delegate))

actual fun Firebase.firestore(app: FirebaseApp, database: String): FirebaseFirestore =
    FirebaseFirestore(NativeFirebase.firestore(app.delegate, database))

actual fun Firebase.firestore(database: String): FirebaseFirestore =
    FirebaseFirestore(NativeFirebase.firestore(database))
