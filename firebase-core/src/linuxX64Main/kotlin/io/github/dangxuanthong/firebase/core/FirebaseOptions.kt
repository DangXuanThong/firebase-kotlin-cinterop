package io.github.dangxuanthong.firebase.core

import io.github.dangxuanthong.firebase.core.native.AppOptions_create
import io.github.dangxuanthong.firebase.core.native.AppOptions_getApiKey
import io.github.dangxuanthong.firebase.core.native.AppOptions_getAppId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getDatabaseUrl
import io.github.dangxuanthong.firebase.core.native.AppOptions_getFcmSenderId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getGaTrackingId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getProjectId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getStorageBucket
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString

internal fun FirebaseOptions.toNative(): CPointer<cnames.structs.NativeAppOptions> =
    AppOptions_create(
        applicationId.cstr,
        apiKey.cstr,
        gcmSenderId?.cstr,
        databaseUrl?.cstr,
        gaTrackingId?.cstr,
        storageBucket?.cstr,
        projectId.cstr
    )!!

internal fun FirebaseOptions.Companion.fromNative(native: CPointer<cnames.structs.NativeAppOptions>) =
    FirebaseOptions(
        applicationId = AppOptions_getAppId(native)!!.toKString(),
        apiKey = AppOptions_getApiKey(native)!!.toKString(),
        projectId = AppOptions_getProjectId(native)!!.toKString(),
        databaseUrl = AppOptions_getDatabaseUrl(native)?.toKString(),
        gaTrackingId = AppOptions_getGaTrackingId(native)?.toKString(),
        storageBucket = AppOptions_getStorageBucket(native)?.toKString(),
        gcmSenderId = AppOptions_getFcmSenderId(native)?.toKString()
    )
