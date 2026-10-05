package com.dangxuanthong.firebase.firestore

import dev.gitlive.firebase.firestore.Source as RealSource

fun Source.toRealSource() = when (this) {
    Source.DEFAULT -> RealSource.DEFAULT
    Source.SERVER -> RealSource.SERVER
    Source.CACHE -> RealSource.CACHE
}
