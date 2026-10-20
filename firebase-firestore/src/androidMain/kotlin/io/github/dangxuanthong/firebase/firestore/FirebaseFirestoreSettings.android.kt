package io.github.dangxuanthong.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreSettings as NativeFirestoreSettings

fun FirebaseFirestoreSettings.toNative(): NativeFirestoreSettings =
    NativeFirestoreSettings.Builder().apply {
        host = host
        isSslEnabled = sslEnabled
        setLocalCacheSettings(cacheSettings.toNative())
    }.build()
