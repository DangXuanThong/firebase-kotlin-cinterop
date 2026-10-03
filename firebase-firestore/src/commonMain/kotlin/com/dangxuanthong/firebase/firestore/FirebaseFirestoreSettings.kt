package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.FirebaseFirestoreSettings`
 *
 * Settings used to configure a [FirebaseFirestore] instance.
 */
expect class FirebaseFirestoreSettings {

    /** Whether to use SSL for communication. */
    val sslEnabled: Boolean

    /** The host of the Cloud Firestore backend. */
    val host: String

    /**
     * The cache settings configured for the SDK. If it is not configured,
     * a default [LocalCacheSettings.Persistent] instance is used.
     */
    val cacheSettings: LocalCacheSettings

    companion object {
        /** Constant to use with [cacheSettings] to disable garbage collection. */
        val CACHE_SIZE_UNLIMITED: Long
        internal val DEFAULT_HOST: String
        internal val MINIMUM_CACHE_BYTES: Long
        internal val DEFAULT_CACHE_SIZE_BYTES: Long
    }

    class Builder {
        var sslEnabled: Boolean
        var host: String
        var cacheSettings: LocalCacheSettings

        internal fun build(): FirebaseFirestoreSettings
    }
}

expect fun firestoreSettings(block: FirebaseFirestoreSettings.Builder.() -> Unit):
    FirebaseFirestoreSettings
