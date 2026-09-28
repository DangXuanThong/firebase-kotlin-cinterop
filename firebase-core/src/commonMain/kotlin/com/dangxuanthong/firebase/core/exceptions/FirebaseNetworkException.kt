package com.dangxuanthong.firebase.core.exceptions

/**
 * Exception thrown when a request to a Firebase service has failed due to a network error. Inspect
 * the device's network connectivity state or retry later to resolve.
 */
class FirebaseNetworkException(override val message: String) : FirebaseException(message)
