package com.dangxuanthong.firebase.firestore

import com.dangxuanthong.firebase.firestore.mappers.toRealFirebaseFirestoreSettings
import dev.gitlive.firebase.firestore.FirebaseFirestore as RealFirebaseFirestore

actual class FirebaseFirestore(private val delegate: RealFirebaseFirestore) {
    actual var settings: FirebaseFirestoreSettings
        @Deprecated("Property can only be written.", level = DeprecationLevel.ERROR)
        get() = throw NotImplementedError()
        set(value) {
            delegate.settings = value.toRealFirebaseFirestoreSettings()
        }

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(delegate.collection(collectionPath))

    actual fun document(documentPath: String): DocumentReference =
        DocumentReference(delegate.document(documentPath))

    actual suspend fun terminate() = delegate.terminate()
}
