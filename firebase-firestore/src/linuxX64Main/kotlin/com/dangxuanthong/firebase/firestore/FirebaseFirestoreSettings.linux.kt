package com.dangxuanthong.firebase.firestore

@ConsistentCopyVisibility
actual data class FirebaseFirestoreSettings internal constructor(
    actual val sslEnabled: Boolean,
    actual val host: String,
    actual val cacheSettings: LocalCacheSettings
) {

    actual class Builder internal constructor(
        actual var sslEnabled: Boolean = true,
        actual var host: String = DEFAULT_HOST,
        actual var cacheSettings: LocalCacheSettings = PersistentCacheSettings()
    ) {
        internal actual fun build(): FirebaseFirestoreSettings {
            check(this.sslEnabled || this.host != DEFAULT_HOST) {
                "You can't disable ssl setting unless you also set a non-default 'host'."
            }
            return FirebaseFirestoreSettings(
                sslEnabled,
                host,
                cacheSettings
            )
        }
    }

    actual companion object {
        actual const val CACHE_SIZE_UNLIMITED = -1L
        internal actual const val DEFAULT_HOST = "firestore.googleapis.com"
        internal actual const val MINIMUM_CACHE_BYTES = 1L * 1024 * 1024 // 1MB
        internal actual const val DEFAULT_CACHE_SIZE_BYTES = 100L * 1024 * 1024 // 100MB
    }
}

actual fun firestoreSettings(block: FirebaseFirestoreSettings.Builder.() -> Unit) =
    FirebaseFirestoreSettings.Builder().apply(block).build()
