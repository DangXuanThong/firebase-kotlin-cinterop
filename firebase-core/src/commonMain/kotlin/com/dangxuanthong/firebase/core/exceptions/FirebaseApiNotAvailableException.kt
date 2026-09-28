package com.dangxuanthong.firebase.core.exceptions

/**
 * Exception thrown when a Firebase API is not available on the current platform or configuration.
 */
class FirebaseApiNotAvailableException(override val message: String) : FirebaseException(message)
