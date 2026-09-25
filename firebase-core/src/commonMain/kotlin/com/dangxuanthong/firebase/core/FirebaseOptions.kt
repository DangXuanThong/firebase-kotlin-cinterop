package com.dangxuanthong.firebase.core

data class FirebaseOptions(
    /** The Google App ID that is used to uniquely identify an instance of an app. */
    val applicationId: String,
    /**
     * API key used for authenticating requests from your app, for example
     * AIzaSyDdVgKwhZl0sTTTLZ7iTmt1r3N2cJLnaDk, used to identify your app to Google servers.
     */
    val apiKey: String,
    /** The database root URL, for example http://abc-xyz-123.firebaseio.com. */
    val databaseUrl: String?,
    /**
     * The tracking ID for Google Analytics, for example UA-12345678-1, used to configure Google Analytics.
     *
     * @hide
     */
    val gaTrackingId: String?,
    /** The Google Cloud Storage bucket name, for example abc-xyz-123.storage.firebase.com. */
    val storageBucket: String?,
    /** The Google Cloud project ID, for example my-project-1234 */
    val projectId: String?,
    /**
     * The Project Number from the Google Developer's console, for example 012345678901, used to
     * configure Google Cloud Messaging.
     */
    val gcmSenderId: String?
)

class FirestoreConfigBuilder internal constructor() {
    var applicationId: String = ""
    var apiKey: String = ""
    var databaseUrl: String? = null
    var gaTrackingId: String? = null
    var storageBucket: String? = null
    var projectId: String? = null
    var gcmSenderId: String? = null
    internal fun build() = FirebaseOptions(
        applicationId,
        apiKey,
        databaseUrl,
        gaTrackingId,
        storageBucket,
        projectId,
        gcmSenderId
    )
}
