package com.dangxuanthong.firestore

import android.content.Context
import dev.gitlive.firebase.Firebase as RealFirebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.firestore.CollectionReference as RealCollectionReference
import dev.gitlive.firebase.firestore.DocumentReference as RealDocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot as RealDocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore as RealFirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import kotlinx.serialization.DeserializationStrategy

actual class FirebaseFirestore(private val delegate: RealFirebaseFirestore) {
    actual fun collection(path: String) = CollectionReference(delegate.collection(path))

    actual fun close() {}
}

actual class CollectionReference(private val delegate: RealCollectionReference) {
    actual fun document(id: String) = DocumentReference(delegate.document(id))
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

actual object Firebase

actual fun Firebase.initialize(context: Any?, config: FirestoreConfig): FirebaseFirestore {
    check(context is Context) { "Android requires a real Context to initialize Firestore" }
    RealFirebase.apps(context).firstOrNull() ?: RealFirebase.initialize(
        context,
        FirebaseOptions(
            applicationId = config.applicationId,
            apiKey = config.apiKey,
            databaseUrl = config.databaseUrl,
            projectId = config.projectId,
            storageBucket = config.storageBucket,
            gaTrackingId = config.gaTrackingId,
            gcmSenderId = config.gcmSenderId,
            authDomain = config.authDomain
        )
    )
    return FirebaseFirestore(RealFirebase.firestore)
}
