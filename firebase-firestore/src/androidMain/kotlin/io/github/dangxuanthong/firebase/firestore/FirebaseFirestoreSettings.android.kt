package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreSettings as NativeFirestoreSettings

fun FirebaseFirestoreSettings.Companion.fromNative(native: NativeFirestoreSettings) =
    FirebaseFirestoreSettings(
        sslEnabled = native.isSslEnabled,
        host = native.host,
        cacheSettings = native.cacheSettings.let { cacheSettings ->
            when (cacheSettings) {
                is com.google.firebase.firestore.MemoryCacheSettings ->
                    MemoryCacheSettings(
                        garbageCollectorSettings = cacheSettings.garbageCollectorSettings.let {
                            when (it) {
                                is com.google.firebase.firestore.MemoryEagerGcSettings ->
                                    MemoryEagerGcSettings

                                is com.google.firebase.firestore.MemoryLruGcSettings ->
                                    MemoryLruGcSettings(it.sizeBytes)

                                else -> MemoryEagerGcSettings
                            }
                        }
                    )

                is com.google.firebase.firestore.PersistentCacheSettings ->
                    PersistentCacheSettings(cacheSettings.sizeBytes)

                else -> PersistentCacheSettings()
            }
        }
    )

fun FirebaseFirestoreSettings.toNative(): NativeFirestoreSettings =
    NativeFirestoreSettings.Builder().apply {
        host = host
        isSslEnabled = sslEnabled
        setLocalCacheSettings(cacheSettings.toNative())
    }.build()
