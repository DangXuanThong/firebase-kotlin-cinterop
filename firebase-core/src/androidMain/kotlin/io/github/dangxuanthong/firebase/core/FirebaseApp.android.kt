package io.github.dangxuanthong.firebase.core

import com.google.firebase.FirebaseApp as NativeFirebaseApp

actual class FirebaseApp internal constructor(private val delegate: NativeFirebaseApp) {

    actual val name: String
        get() = delegate.name

    actual val options: FirebaseOptions
        get() = FirebaseOptions.fromNative(delegate.options)

    actual fun delete(): Unit = delegate.delete()

    override fun equals(other: Any?): Boolean = other is FirebaseApp && other.delegate == delegate
    override fun hashCode(): Int = delegate.hashCode()
    override fun toString(): String = delegate.toString()
}
