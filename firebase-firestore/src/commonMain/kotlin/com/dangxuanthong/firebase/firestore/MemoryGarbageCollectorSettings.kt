package com.dangxuanthong.firebase.firestore

/** Mirroring `dev.gitlive.firebase.firestore.MemoryGarbageCollectorSettings` */
sealed interface MemoryGarbageCollectorSettings {

    /**
     * Configures the SDK to use an eager garbage collector for memory cache. The eager garbage
     * collector will attempt to remove any documents from SDK's memory cache as soon as it is no longer
     * used.
     *
     * This is the default garbage collector unless specified explicitly otherwise.
     */
    data object Eager : MemoryGarbageCollectorSettings

    /**
     * Configures the SDK to use a Least-Recently-Used garbage collector for memory cache.
     */
    data class LruGC(
        /**
         * Cache size threshold for the memory cache. If the cache grows beyond this size,
         * Firestore SDK will start removing data that hasn't been recently used.
         * The size is not a guarantee that the cache will stay below that size,
         * only that if the cache exceeds the given size, cleanup will be attempted.
         *
         * By default, memory LRU cache is enabled with a cache size of 100MB (100 * 1024 * 1024).
         * The minimum value is 1 MB (1024 * 1024).
         */
        val sizeBytes: Long = FirebaseFirestoreSettings.DEFAULT_CACHE_SIZE_BYTES
    ) : MemoryGarbageCollectorSettings
}

typealias MemoryEagerGcSettings = MemoryGarbageCollectorSettings.Eager
typealias MemoryLruGcSettings = MemoryGarbageCollectorSettings.LruGC
