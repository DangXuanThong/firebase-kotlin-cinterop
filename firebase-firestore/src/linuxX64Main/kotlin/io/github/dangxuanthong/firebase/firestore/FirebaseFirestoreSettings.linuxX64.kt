package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeSettings
import io.github.dangxuanthong.firebase.core.native.Settings_create
import io.github.dangxuanthong.firebase.core.native.Settings_destroy
import io.github.dangxuanthong.firebase.core.native.Settings_getCacheSizeBytes
import io.github.dangxuanthong.firebase.core.native.Settings_getHost
import io.github.dangxuanthong.firebase.core.native.Settings_isPersistenceEnabled
import io.github.dangxuanthong.firebase.core.native.Settings_isSslEnabled
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString

fun FirebaseFirestoreSettings.Companion.fromNative(native: CPointer<NativeSettings>) =
    FirebaseFirestoreSettings(
        sslEnabled = Settings_isSslEnabled(native),
        host = Settings_getHost(native)?.toKString() ?: DEFAULT_HOST,
        cacheSettings = if (Settings_isPersistenceEnabled(native))
            PersistentCacheSettings(Settings_getCacheSizeBytes(native))
        else MemoryCacheSettings()
    )

fun FirebaseFirestoreSettings.toNative(): CPointer<NativeSettings> =
    Settings_create(
        host = host.cstr,
        ssl_enabled = sslEnabled,
        persistence_enabled = this.cacheSettings is LocalCacheSettings.Persistent,
        cache_size_bytes = if (cacheSettings is LocalCacheSettings.Persistent)
            cacheSettings.sizeBytes else 1
    )!!

fun <T> CPointer<NativeSettings>.use(block: (CPointer<NativeSettings>) -> T): T =
    try {
        block(this)
    } finally {
        Settings_destroy(this)
    }
