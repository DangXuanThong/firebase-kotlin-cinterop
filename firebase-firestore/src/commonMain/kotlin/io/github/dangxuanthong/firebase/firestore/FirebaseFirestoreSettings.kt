package io.github.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.FirebaseFirestoreSettings`
 *
 * Settings used to configure a [FirebaseFirestore] instance.
 */
data class FirebaseFirestoreSettings(
    /** Whether to use SSL for communication. */
    val sslEnabled: Boolean = true,

    /** The host of the Cloud Firestore backend. */
    val host: String = DEFAULT_HOST,

    /**
     * The cache settings configured for the SDK. If it is not configured,
     * a default [LocalCacheSettings.Persistent] instance is used.
     */
    val cacheSettings: LocalCacheSettings = PersistentCacheSettings()
) {

    init {
        check(sslEnabled || host != DEFAULT_HOST) {
            "You can't disable ssl setting unless you also set a non-default 'host'."
        }
    }

    companion object {
        /** Constant to use with [cacheSettings] to disable garbage collection. */
        const val CACHE_SIZE_UNLIMITED = -1L
        const val DEFAULT_HOST = "firestore.googleapis.com"
        internal const val MINIMUM_CACHE_BYTES = 1L * 1024 * 1024 // 1MB
        internal const val DEFAULT_CACHE_SIZE_BYTES = 100L * 1024 * 1024 // 100MB
    }
}
