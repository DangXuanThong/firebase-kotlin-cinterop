package com.dangxuanthong.firebase.core.exceptions

private const val UNKNOWN_MESSAGE = "Unknown Firebase error"

/**
 * Exception that gets thrown when an operation on Firebase fails.
 *
 * An empty [message] is replaced with a generic one rather than rejected: messages often
 * come straight from a native SDK, and a failure with no text should still surface as a
 * [FirebaseException], not as an `IllegalArgumentException` thrown by the constructor.
 */
open class FirebaseException(message: String, cause: Throwable? = null) :
    Exception(message.ifEmpty { UNKNOWN_MESSAGE }, cause) {

    override val message: String
        get() = super.message ?: UNKNOWN_MESSAGE
}
