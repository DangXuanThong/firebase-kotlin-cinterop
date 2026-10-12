package com.dangxuanthong.firebase.firestore

expect class CollectionReference {
    fun document(id: String): DocumentReference
}
