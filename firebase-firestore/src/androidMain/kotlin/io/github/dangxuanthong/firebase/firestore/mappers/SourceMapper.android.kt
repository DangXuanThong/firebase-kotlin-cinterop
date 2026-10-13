package io.github.dangxuanthong.firebase.firestore.mappers

import dev.gitlive.firebase.firestore.Source as RealSource
import io.github.dangxuanthong.firebase.firestore.Source

fun Source.toRealSource() = when (this) {
    Source.DEFAULT -> RealSource.DEFAULT
    Source.SERVER -> RealSource.SERVER
    Source.CACHE -> RealSource.CACHE
}
