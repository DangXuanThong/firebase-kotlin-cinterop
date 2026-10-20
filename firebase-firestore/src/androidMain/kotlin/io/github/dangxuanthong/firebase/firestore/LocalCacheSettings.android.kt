package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.firestore.LocalCacheSettings as NativeLocalCacheSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.MemoryEagerGcSettings
import com.google.firebase.firestore.MemoryLruGcSettings
import com.google.firebase.firestore.PersistentCacheSettings

fun LocalCacheSettings.toNative(): NativeLocalCacheSettings = when (this) {
    is LocalCacheSettings.Memory -> MemoryCacheSettings.newBuilder().apply {
        val gcSettings = when (this@toNative.garbageCollectorSettings) {
            MemoryGarbageCollectorSettings.Eager -> MemoryEagerGcSettings.newBuilder().build()

            is MemoryGarbageCollectorSettings.LruGC -> MemoryLruGcSettings.newBuilder()
                .setSizeBytes(this@toNative.garbageCollectorSettings.sizeBytes).build()
        }
        setGcSettings(gcSettings)
    }.build()

    is LocalCacheSettings.Persistent -> PersistentCacheSettings.newBuilder()
        .setSizeBytes(this.sizeBytes).build()
}
