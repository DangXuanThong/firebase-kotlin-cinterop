package com.dangxuanthong.firebase.core.exceptions

/**
 * Exception thrown when a request to a Firebase service has been blocked due to having received too
 * many consecutive requests from the same device. Retry the request later to resolve.
 */
class FirebaseTooManyRequestsException(override val message: String) : FirebaseException(message)
