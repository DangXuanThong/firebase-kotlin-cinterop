package com.dangxuanthong.firebase.firestore

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer

expect class FirebaseFirestore {
    fun collection(path: String): CollectionReference
    fun close()
}

expect class CollectionReference {
    fun document(id: String): DocumentReference
}

expect class DocumentReference {
    val id: String
    suspend fun get(): DocumentSnapshot
}

expect class DocumentSnapshot {
    val exists: Boolean
    suspend fun <T> data(strategy: DeserializationStrategy<T>): T
}

suspend inline fun <reified T> DocumentSnapshot.data(): T = data(serializer())
