package io.github.dangxuanthong.firebase.firestore

import dev.gitlive.firebase.Firebase as RealFirebase
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import dev.gitlive.firebase.firestore.firestore
import io.github.dangxuanthong.firebase.core.Firebase

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) =
        DocumentReference(delegate.document(id))
}

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(RealFirebase.firestore)
