package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.core.Firebase
import dev.gitlive.firebase.Firebase as RealFirebase
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import dev.gitlive.firebase.firestore.firestore

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) =
        DocumentReference(delegate.document(id))
}

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(RealFirebase.firestore)
