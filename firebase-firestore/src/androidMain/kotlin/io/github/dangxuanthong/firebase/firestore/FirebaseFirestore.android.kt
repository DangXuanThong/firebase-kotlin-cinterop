package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore as NativeFirebaseFirestore

actual class FirebaseFirestore internal constructor(private val delegate: NativeFirebaseFirestore) {

    actual var settings: FirebaseFirestoreSettings
        get() = FirebaseFirestoreSettings.fromNative(delegate.firestoreSettings)
        set(value) {
            delegate.firestoreSettings = value.toNative()
        }

//    actual fun collection(collectionPath: String): CollectionReference =
//        CollectionReference(delegate.collection(collectionPath))
//
//    actual fun document(documentPath: String): DocumentReference =
//        DocumentReference(delegate.document(documentPath))

//    actual suspend fun terminate() {
//        delegate.terminate()
//    }
}
