package io.github.dangxuanthong.firebase.firestore

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore as NativeFirebaseFirestore
import com.google.firebase.firestore.model.DatabaseId
import io.github.dangxuanthong.firebase.core.FirebaseApp

actual class FirebaseFirestore private constructor(private val delegate: NativeFirebaseFirestore) {

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

    actual companion object {
        @SuppressLint("RestrictedApi")
        actual const val DEFAULT_DATABASE: String = DatabaseId.DEFAULT_DATABASE_ID

        actual fun getInstance(app: FirebaseApp, database: String): FirebaseFirestore =
            FirebaseFirestore(NativeFirebaseFirestore.getInstance(app.delegate, database))
    }
}
