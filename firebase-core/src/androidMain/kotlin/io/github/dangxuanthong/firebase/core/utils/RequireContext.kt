package io.github.dangxuanthong.firebase.core.utils

import android.content.Context

internal fun Any?.requireContext(): Context {
    require(this is Context) {
        "Android requires a real android.content.Context to use Firebase SDK"
    }
    return this
}
