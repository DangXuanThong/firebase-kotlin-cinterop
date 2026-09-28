package com.dangxuanthong.firebase.core

data class FirebaseOptions(
    /** The Google App ID that is used to uniquely identify an instance of an app. */
    val applicationId: String,
    /**
     * API key used for authenticating requests from your app, for example
     * AIzaSyDdVgKwhZl0sTTTLZ7iTmt1r3N2cJLnaDk, used to identify your app to Google servers.
     */
    val apiKey: String,
    /** The Google Cloud project ID, for example my-project-1234 */
    val projectId: String,
    /** The database root URL, for example http://abc-xyz-123.firebaseio.com. */
    val databaseUrl: String? = null,
    /**
     * The tracking ID for Google Analytics, for example UA-12345678-1, used to configure Google Analytics.
     */
    val gaTrackingId: String? = null,
    /** The Google Cloud Storage bucket name, for example abc-xyz-123.storage.firebase.com. */
    val storageBucket: String? = null,
    /**
     * The Project Number from the Google Developer's console, for example 012345678901, used to
     * configure Google Cloud Messaging.
     */
    val gcmSenderId: String? = null
)

/** Receiver of the lambda passed to `Firebase.initialize { ... }`. */
class FirebaseOptionsBuilder internal constructor() {
    var applicationId: String = ""
    var apiKey: String = ""
    var projectId: String = ""
    var databaseUrl: String? = null
    var gaTrackingId: String? = null
    var storageBucket: String? = null
    var gcmSenderId: String? = null

    internal fun build(): FirebaseOptions {
        // Fail here instead of letting empty values reach the SDK, where a bad
        // config surfaces as a generic "client is offline" error.
        require(applicationId.isNotBlank()) { "applicationId must be set" }
        require(apiKey.isNotBlank()) { "apiKey must be set" }
        require(projectId.isNotBlank()) { "projectId must be set" }
        return FirebaseOptions(
            applicationId,
            apiKey,
            projectId,
            databaseUrl,
            gaTrackingId,
            storageBucket,
            gcmSenderId
        )
    }
}
