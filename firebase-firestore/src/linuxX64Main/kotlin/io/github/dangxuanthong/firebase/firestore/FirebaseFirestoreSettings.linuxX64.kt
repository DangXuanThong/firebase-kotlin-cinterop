package io.github.dangxuanthong.firebase.firestore

import cnames.structs.NativeSettings
import io.github.dangxuanthong.firebase.core.native.Settings_create
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr

fun FirebaseFirestoreSettings.toNative(): CPointer<NativeSettings> =
    Settings_create(
        host = host.cstr,
        ssl_enabled = sslEnabled,
        persistence_enabled = this.cacheSettings is LocalCacheSettings.Persistent,
        cache_size_bytes = if (cacheSettings is LocalCacheSettings.Persistent)
            cacheSettings.sizeBytes else 1
    )!!
