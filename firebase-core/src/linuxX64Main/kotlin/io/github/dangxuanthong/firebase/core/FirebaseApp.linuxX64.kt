package io.github.dangxuanthong.firebase.core

import io.github.dangxuanthong.firebase.core.native.App_destroy
import io.github.dangxuanthong.firebase.core.native.App_getName
import io.github.dangxuanthong.firebase.core.native.App_getOptions
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking

actual class FirebaseApp internal constructor(delegate: CPointer<cnames.structs.NativeApp>) :
    AutoCloseable {

    private var delegate: CPointer<cnames.structs.NativeApp>?

    actual val name: String
        get() = App_getName(delegate)?.toKString()
            ?: throw IllegalStateException("FirebaseApp was deleted")

    actual val options: FirebaseOptions
        get() = App_getOptions(delegate)?.let { FirebaseOptions.fromNative(it) }
            ?: throw IllegalStateException("FirebaseApp was deleted")

//    Private props used in equals/hashCode/toString. This allows those methods to function even
//    when the delegate has been deleted (the same behavior Google sdk uses)
    private val internalName: String
    private val internalOptions: FirebaseOptions

    init {
        this.delegate = delegate
        this.internalName = name
        this.internalOptions = options
    }

    actual suspend fun delete() {
        App_destroy(delegate)
        delegate = null
    }

    override fun close() = runBlocking {
        delete()
    }

    override fun equals(other: Any?): Boolean =
        other is FirebaseApp && other.name == internalName
    override fun hashCode(): Int = internalName.hashCode()
    override fun toString() = "FirebaseApp(name=$internalName, options=$internalOptions)"
}
