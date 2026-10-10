package com.dangxuanthong.firebase.firestore.exceptions

import com.dangxuanthong.firebase.core.exceptions.FirebaseException

/** A class of exceptions thrown by Cloud Firestore. */
sealed class FirestoreException(override val message: String, cause: Throwable? = null) :
    FirebaseException(message, cause) {

    /**
     * The operation completed successfully. `FirebaseFirestoreException` will never have a
     * status of `Ok`.
     */
    class Ok : FirestoreException("Ok")

    /** The operation was cancelled (typically by the caller). */
    class Cancelled(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Unknown error or an error from a different error domain. */
    class Unknown(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * Client specified an invalid argument.
     */
    class InvalidArgument(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * Deadline expired before operation could complete. For operations that change the state of the
     * system, this error may be returned even if the operation has completed successfully. For
     * example, a successful response from a server could have been delayed long enough for the
     * deadline to expire.
     */
    class DeadlineExceeded(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Some requested document was not found. */
    class NotFound(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Some document that we attempted to create already exists. */
    class AlreadyExists(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** The caller does not have permission to execute the specified operation. */
    class PermissionDenied(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system
     * is out of space.
     */
    class ResourceExhausted(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * Operation was rejected because the system is not in a state required for the operation's
     * execution.
     */
    class FailedPrecondition(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * The operation was aborted, typically due to a concurrency issue like transaction aborts, etc.
     */
    class Aborted(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Operation was attempted past the valid range. */
    class OutOfRange(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Operation is not implemented or not supported/enabled. */
    class Unimplemented(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * Internal errors. Means some invariants expected by underlying system has been broken. If you
     * see one of these errors, something is very broken.
     */
    class Internal(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /**
     * The service is currently unavailable. This is a most likely a transient condition and may be
     * corrected by retrying with a backoff.
     */
    class Unavailable(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** Unrecoverable data loss or corruption. */
    class DataLoss(message: String, cause: Throwable? = null) :
        FirestoreException(message, cause)

    /** The request does not have valid authentication credentials for the operation. */
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
