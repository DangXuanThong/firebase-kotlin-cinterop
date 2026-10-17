package io.github.dangxuanthong.firebase.core

import com.google.firebase.FirebaseOptions as NativeFirebaseOptions

internal fun FirebaseOptions.toNative(): NativeFirebaseOptions =
    NativeFirebaseOptions.Builder().run {
        setApplicationId(applicationId)
        setApiKey(apiKey)
        setProjectId(projectId)
        setDatabaseUrl(databaseUrl)
        setGaTrackingId(gaTrackingId)
        setStorageBucket(storageBucket)
        setGcmSenderId(gcmSenderId)
        build()
    }

internal fun FirebaseOptions.Companion.fromNative(native: NativeFirebaseOptions) =
    with(native) {
        FirebaseOptions(
            applicationId = applicationId,
            apiKey = apiKey,
            projectId = projectId!!,
            databaseUrl = databaseUrl,
            gaTrackingId = gaTrackingId,
            storageBucket = storageBucket,
            gcmSenderId = gcmSenderId
        )
    }
