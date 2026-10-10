package com.dangxuanthong.firebase.firestore.utils

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef

/**
 * Executes [block] with the target [T] dereferenced from this [COpaquePointer] [StableRef],
 * automatically disposing the reference in a `finally` block when finished.
 */
internal inline fun <reified T : Any> COpaquePointer.useAsStableRef(block: (T) -> Unit) =
    this.asStableRef<T>().let {
        try {
            block(it.get())
        } finally {
            it.dispose()
        }
    }
