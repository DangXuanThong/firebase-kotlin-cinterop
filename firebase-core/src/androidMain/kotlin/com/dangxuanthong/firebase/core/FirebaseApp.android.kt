package com.dangxuanthong.firebase.core

import dev.gitlive.firebase.FirebaseApp as RealFirebaseApp

actual class FirebaseApp internal constructor(private val delegate: RealFirebaseApp) {

    actual val name: String
        get() = delegate.name

    actual val options: FirebaseOptions
        get() = with(delegate.options) {
            FirebaseOptions(
                applicationId = applicationId,
                apiKey = apiKey,
                databaseUrl = databaseUrl,
                gaTrackingId = gaTrackingId,
                storageBucket = storageBucket,
                projectId = projectId,
                gcmSenderId = gcmSenderId
            )
        }

    actual suspend fun delete(): Unit = delegate.delete()

    override fun equals(other: Any?): Boolean =
        other is FirebaseApp && other.delegate == delegate

    override fun hashCode(): Int = delegate.hashCode()

    override fun toString(): String = delegate.toString()
}
