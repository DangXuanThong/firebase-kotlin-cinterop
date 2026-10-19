package io.github.dangxuanthong.firebase.core

import androidx.test.platform.app.InstrumentationRegistry

internal actual val testContext: Any?
    get() = InstrumentationRegistry.getInstrumentation().targetContext
