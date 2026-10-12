package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.core.FirebaseApp
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import dev.gitlive.firebase.firestore.firestore

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) =
        DocumentReference(delegate.document(id))
}

val FirebaseApp.firestore: FirebaseFirestore
    get() = FirebaseFirestore(dev.gitlive.firebase.Firebase.firestore)
