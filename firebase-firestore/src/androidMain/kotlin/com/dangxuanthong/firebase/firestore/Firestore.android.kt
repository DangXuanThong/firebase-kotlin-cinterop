package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.core.FirebaseApp
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import dev.gitlive.firebase.firestore.DocumentReference as RealDocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot as RealDocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore as RealFirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.DeserializationStrategy

actual class FirebaseFirestore(private val delegate: RealFirebaseFirestore) {
    actual fun collection(path: String) =
        CollectionReference(delegate.collection(path))

    actual fun close() {}
}

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) =
        DocumentReference(delegate.document(id))
}

actual class DocumentReference(private val delegate: RealDocumentReference) {
    actual val id: String get() = delegate.id
    actual suspend fun get() = DocumentSnapshot(delegate.get())
}

actual class DocumentSnapshot(private val delegate: RealDocumentSnapshot) {
    actual val exists: Boolean
        get() = delegate.exists

    actual suspend fun <T> data(strategy: DeserializationStrategy<T>): T = delegate.data(strategy)
}

val FirebaseApp.firestore: FirebaseFirestore
    get() = FirebaseFirestore(dev.gitlive.firebase.Firebase.firestore)
