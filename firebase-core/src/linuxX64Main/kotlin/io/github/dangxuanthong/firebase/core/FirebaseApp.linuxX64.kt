package io.github.dangxuanthong.firebase.core

import cnames.structs.NativeApp
import io.github.dangxuanthong.firebase.core.native.App_create
import io.github.dangxuanthong.firebase.core.native.App_destroy
import io.github.dangxuanthong.firebase.core.native.App_getDefaultAppName
import io.github.dangxuanthong.firebase.core.native.App_getName
import io.github.dangxuanthong.firebase.core.native.App_getOptions
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndUpdate
import kotlin.concurrent.atomics.updateAndFetch
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalAtomicApi::class)
actual class FirebaseApp private constructor(delegate: CPointer<NativeApp>) : AutoCloseable {

    private var delegate: CPointer<NativeApp>?

    actual val name: String
        get() = App_getName(delegate)?.toKString()
            ?: throw IllegalStateException("FirebaseApp was deleted")

    actual val options: FirebaseOptions
        get() = App_getOptions(delegate)?.let { FirebaseOptions.fromNative(it) }
            ?: throw IllegalStateException("FirebaseApp was deleted")

    // Private props used in equals/hashCode/toString. This allows those methods to function even
    // when the delegate has been deleted (the same behavior Google sdk uses)
    private val internalName: String
    private val internalOptions: FirebaseOptions

    init {
        this.delegate = delegate
        this.internalName = name
        this.internalOptions = options
    }

    actual suspend fun delete() {
        INSTANCES.fetchAndUpdate { current ->
            App_destroy(delegate)
            delegate = null
            current - internalName
        }
    }

    override fun close() = runBlocking {
        delete()
    }

    override fun equals(other: Any?): Boolean = other is FirebaseApp && other.name == internalName
    override fun hashCode(): Int = internalName.hashCode()
    override fun toString() = "FirebaseApp(name=$internalName, options=$internalOptions)"

    internal companion object {
        val DEFAULT_APP_NAME: String
            get() = App_getDefaultAppName()!!.toKString()

        private val INSTANCES = AtomicReference(mapOf<String, FirebaseApp>())

        fun getInstance(name: String = DEFAULT_APP_NAME): FirebaseApp = INSTANCES.load().let {
            if (it.containsKey(name)) return@let it[name]!!

            if (name == DEFAULT_APP_NAME) throw IllegalStateException(
                "Default FirebaseApp is not initialized " +
                    "in this process. Make sure to call FirebaseApp.initializeApp() first."
            )

            val availableAppNamesMessage = it.keys.takeIf { it.isNotEmpty() }
                ?.let { "Available app names: " + it.joinToString() } ?: ""
            throw IllegalStateException(
                "FirebaseApp with name $name doesn't exist. $availableAppNamesMessage"
            )
        }

        fun initializeApp(options: FirebaseOptions, name: String = DEFAULT_APP_NAME): FirebaseApp {
            val normalizedName = name.trim()
            check(!INSTANCES.load().containsKey(name)) {
                "FirebaseApp name $name already exists!"
            }

            return INSTANCES.updateAndFetch {
                val nativeApp = App_create(options.toNative(), normalizedName.cstr)
                    ?: throw IllegalStateException(
                        "Failed to initialize native FirebaseApp instance."
                    )
                val app = FirebaseApp(nativeApp)
                it + (name to app)
            }[name]!!
        }

        val apps: List<FirebaseApp>
            get() = INSTANCES.load().values.toList()
    }
}
