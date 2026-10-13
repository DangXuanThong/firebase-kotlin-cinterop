package io.github.dangxuanthong.firebase.firestore.mappers

import dev.gitlive.firebase.firestore.FirebaseFirestoreException as RealFirestoreException
import io.github.dangxuanthong.firebase.firestore.exceptions.FirestoreException

internal fun RealFirestoreException.toFirestoreException() = FirestoreException.fromCode(
    code.ordinal,
    // gitlive's sdk on android is just an alias to google's sdk, so message must be non-null here
    message!!,
    cause
)
