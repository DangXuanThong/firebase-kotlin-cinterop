package com.dangxuanthong.firebase.core.exceptions

/**
 * Exception that gets thrown when an operation on Firebase fails.
 */
open class FirebaseException(override val message: String, override val cause: Throwable? = null) :
    Exception(message, cause) {

    init {
        require(message.isNotEmpty()) { "Detail message must not be empty" }
    }
}
