package io.github.dangxuanthong.firebase.core

import cnames.structs.NativeAppOptions
import io.github.dangxuanthong.firebase.core.native.AppOptions_create
import io.github.dangxuanthong.firebase.core.native.AppOptions_destroy
import io.github.dangxuanthong.firebase.core.native.AppOptions_getApiKey
import io.github.dangxuanthong.firebase.core.native.AppOptions_getAppId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getDatabaseUrl
import io.github.dangxuanthong.firebase.core.native.AppOptions_getFcmSenderId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getGaTrackingId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getProjectId
import io.github.dangxuanthong.firebase.core.native.AppOptions_getStorageBucket
import io.github.dangxuanthong.firebase.core.utils.toKStringOrNull
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString

internal fun FirebaseOptions.toNative(): CPointer<NativeAppOptions> =
    AppOptions_create(
        applicationId.cstr,
        apiKey.cstr,
        gcmSenderId?.cstr,
        databaseUrl?.cstr,
        gaTrackingId?.cstr,
        storageBucket?.cstr,
        projectId.cstr
    )!!

internal fun FirebaseOptions.Companion.fromNative(native: CPointer<NativeAppOptions>) =
    FirebaseOptions(
        applicationId = AppOptions_getAppId(native)!!.toKString(),
        apiKey = AppOptions_getApiKey(native)!!.toKString(),
        projectId = AppOptions_getProjectId(native)!!.toKString(),
        databaseUrl = AppOptions_getDatabaseUrl(native)?.toKStringOrNull(),
        gaTrackingId = AppOptions_getGaTrackingId(native)?.toKStringOrNull(),
        storageBucket = AppOptions_getStorageBucket(native)?.toKStringOrNull(),
        gcmSenderId = AppOptions_getFcmSenderId(native)?.toKStringOrNull()
    )

/**
 * Executes [block] with this temporary [NativeAppOptions] and guarantees cleanup via
 * [AppOptions_destroy]. Used for initializing apps, where the C++ SDK copies the
 * options internally on creation.
 */
internal inline fun <T> CPointer<NativeAppOptions>.use(block: (CPointer<NativeAppOptions>) -> T) =
    try {
        block(this)
    } finally {
        AppOptions_destroy(this)
    }
