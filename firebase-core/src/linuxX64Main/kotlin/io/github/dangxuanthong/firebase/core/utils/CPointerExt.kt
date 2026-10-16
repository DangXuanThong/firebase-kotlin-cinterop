package io.github.dangxuanthong.firebase.core.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

fun CPointer<ByteVar>.toKStringOrNull(): String? = this.toKString().takeIf { it.isNotEmpty() }
