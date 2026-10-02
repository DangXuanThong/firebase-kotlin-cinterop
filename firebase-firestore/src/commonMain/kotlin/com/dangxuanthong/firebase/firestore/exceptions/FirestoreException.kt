package com.dangxuanthong.firebase.firestore.exceptions

import com.dangxuanthong.firebase.core.exceptions.FirebaseException

sealed class FirestoreException(message: String, cause: Throwable? = null) :
    FirebaseException(message, cause) {

    class Ok : FirestoreException("Ok")

    class Cancelled(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Unknown(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class InvalidArgument(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class DeadlineExceeded(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class NotFound(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class AlreadyExists(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class PermissionDenied(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class ResourceExhausted(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class FailedPrecondition(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Aborted(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class OutOfRange(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Unimplemented(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Internal(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Unavailable(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class DataLoss(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    class Unauthenticated(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    companion object {
        internal fun fromCode(
            code: Int,
            message: String,
            cause: Throwable? = null
        ): FirestoreException = when (code) {
            0 -> Ok()
            1 -> Cancelled(message, cause)
            2 -> Unknown(message, cause)
            3 -> InvalidArgument(message, cause)
            4 -> DeadlineExceeded(message, cause)
            5 -> NotFound(message, cause)
            6 -> AlreadyExists(message, cause)
            7 -> PermissionDenied(message, cause)
            8 -> ResourceExhausted(message, cause)
            9 -> FailedPrecondition(message, cause)
            10 -> Aborted(message, cause)
            11 -> OutOfRange(message, cause)
            12 -> Unimplemented(message, cause)
            13 -> Internal(message, cause)
            14 -> Unavailable(message, cause)
            15 -> DataLoss(message, cause)
            16 -> Unauthenticated(message, cause)
            else -> Unknown("[$code] $message", cause)
        }
    }
}
