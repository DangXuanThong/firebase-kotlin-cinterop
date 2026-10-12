package com.dangxuanthong.firebase.firestore.mappers

import com.dangxuanthong.firebase.firestore.FirebaseFirestoreSettings
import dev.gitlive.firebase.firestore.FirebaseFirestoreSettings as RealFirebaseFirestoreSettings

fun FirebaseFirestoreSettings.toRealFirebaseFirestoreSettings(): RealFirebaseFirestoreSettings =
    RealFirebaseFirestoreSettings.Builder().run {
        sslEnabled = this@toRealFirebaseFirestoreSettings.sslEnabled
        host = this@toRealFirebaseFirestoreSettings.host
        // TODO: Port CacheSettings
//        cacheSettings = this@toRealFirebaseFirestoreSettings.cacheSettings
        build()
    }
