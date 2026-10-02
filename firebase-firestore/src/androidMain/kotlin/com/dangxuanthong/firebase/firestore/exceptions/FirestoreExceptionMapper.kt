package com.dangxuanthong.firebase.firestore.exceptions

import dev.gitlive.firebase.firestore.FirebaseFirestoreException as RealFirestoreException

internal fun RealFirestoreException.toFirestoreException() = FirestoreException.fromCode(
    code.ordinal,
    // gitlive's sdk on android is just an alias to google's sdk, so message must be non-null here
    message!!,
    cause
)
