package io.github.dangxuanthong.firebase.core

import androidx.test.platform.app.InstrumentationRegistry

actual val testContext: Any?
    get() = InstrumentationRegistry.getInstrumentation().targetContext
