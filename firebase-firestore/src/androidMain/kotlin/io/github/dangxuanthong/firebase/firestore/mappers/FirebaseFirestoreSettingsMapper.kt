package io.github.dangxuanthong.firebase.firestore.mappers

import dev.gitlive.firebase.firestore.FirebaseFirestoreSettings as RealFirebaseFirestoreSettings
import io.github.dangxuanthong.firebase.firestore.FirebaseFirestoreSettings

fun FirebaseFirestoreSettings.toRealFirebaseFirestoreSettings(): RealFirebaseFirestoreSettings =
    RealFirebaseFirestoreSettings.Builder().run {
        sslEnabled = this@toRealFirebaseFirestoreSettings.sslEnabled
        host = this@toRealFirebaseFirestoreSettings.host
        // TODO: Port CacheSettings
//        cacheSettings = this@toRealFirebaseFirestoreSettings.cacheSettings
        build()
    }
