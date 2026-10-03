package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.LocalCacheSettings`
 *
 * Marker interface implemented by all supported cache settings.
 *
 * [Persistent] and [Memory] are the two only cache types
 * supported by the SDK. Custom implementation is not supported.
 */
sealed interface LocalCacheSettings {

    /**
     * Configures the SDK to use a persistent cache. Firestore documents and mutations are persisted
     * across App restart.
     *
     * This is the default cache type unless explicitly specified otherwise.
     */
    data class Persistent(
        val sizeBytes: Long = FirebaseFirestoreSettings.DEFAULT_CACHE_SIZE_BYTES
    ) : LocalCacheSettings

    /**
     * Configures the SDK to use a memory cache. Firestore documents and mutations are NOT persisted
     * across App restart.
     */
    data class Memory(
        /** The [MemoryGarbageCollectorSettings] object used to configure the SDK cache. */
        val garbageCollectorSettings: MemoryGarbageCollectorSettings = MemoryEagerGcSettings
    ) : LocalCacheSettings
}

typealias PersistentCacheSettings = LocalCacheSettings.Persistent
typealias MemoryCacheSettings = LocalCacheSettings.Memory
