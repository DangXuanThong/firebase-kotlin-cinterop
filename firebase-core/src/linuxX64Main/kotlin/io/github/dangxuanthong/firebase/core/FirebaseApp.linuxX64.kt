package io.github.dangxuanthong.firebase.core

import cnames.structs.NativeApp
import io.github.dangxuanthong.firebase.core.annotations.InternalFirebaseApi
import io.github.dangxuanthong.firebase.core.native.App_create
import io.github.dangxuanthong.firebase.core.native.App_destroy
import io.github.dangxuanthong.firebase.core.native.App_getDefaultAppName
import io.github.dangxuanthong.firebase.core.native.App_getName
import io.github.dangxuanthong.firebase.core.native.App_getOptions
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString

actual class FirebaseApp private constructor(delegate: CPointer<NativeApp>) : AutoCloseable {

    @InternalFirebaseApi
    var delegate: CPointer<NativeApp>? = delegate
        private set

    actual val name: String
        get() = App_getName(delegate)?.toKString()
            ?: throw IllegalStateException("FirebaseApp was deleted")

    actual val options: FirebaseOptions
        get() = App_getOptions(delegate)?.let { FirebaseOptions.fromNative(it) }
            ?: throw IllegalStateException("FirebaseApp was deleted")

    // Private props used in equals/hashCode/toString. This allows those methods to function even
    // when the delegate has been deleted (the same behavior Google sdk uses)
    private val internalName: String = name
    private val internalOptions: FirebaseOptions = options

    actual fun delete() = lock.withLock {
        App_destroy(delegate)
        instances.remove(internalName)
        delegate = null
    }

    override fun close() {
        delete()
    }

    override fun equals(other: Any?): Boolean = other is FirebaseApp && other.name == internalName
    override fun hashCode(): Int = internalName.hashCode()
    override fun toString() = "FirebaseApp(name=$internalName, options=$internalOptions)"

    internal companion object {
        val DEFAULT_APP_NAME: String
            get() = App_getDefaultAppName()!!.toKString()

        private val instances = mutableMapOf<String, FirebaseApp>()
        private val lock = reentrantLock()

        fun getInstance(name: String = DEFAULT_APP_NAME): FirebaseApp = lock.withLock {
            val normalizedName = name.trim()
            if (instances.containsKey(normalizedName)) return instances[normalizedName]!!

            if (normalizedName == DEFAULT_APP_NAME) throw IllegalStateException(
                "Default FirebaseApp is not initialized " +
                    "in this process. Make sure to call Firebase.initialize() first."
            )

            val availableAppNamesMessage = instances.keys.takeIf { it.isNotEmpty() }
                ?.let { " Available app names: " + it.joinToString() } ?: ""
            throw IllegalStateException(
                "FirebaseApp with name $normalizedName doesn't exist.$availableAppNamesMessage"
            )
        }

        fun initializeApp(options: FirebaseOptions, name: String = DEFAULT_APP_NAME): FirebaseApp =
            lock.withLock {
                val normalizedName = name.trim()
                check(!instances.containsKey(normalizedName)) {
                    "FirebaseApp name $name already exists!"
                }

                options.toNative().use { options ->
                    val nativeApp = App_create(options, normalizedName.cstr)
                        ?: throw IllegalStateException(
                            "Failed to initialize native FirebaseApp instance."
                        )
                    instances[name] = FirebaseApp(nativeApp)
                }
                instances[name]!!
            }

        val apps: List<FirebaseApp>
            get() = lock.withLock { instances.values.toList() }
    }
}
