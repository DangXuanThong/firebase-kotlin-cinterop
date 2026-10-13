package io.github.dangxuanthong.firebase.firestore.mappers

import dev.gitlive.firebase.firestore.ServerTimestampBehavior as RealServerTimestampBehavior
import io.github.dangxuanthong.firebase.firestore.ServerTimestampBehavior

fun ServerTimestampBehavior.toRealServerTimestampBehavior(): RealServerTimestampBehavior =
    RealServerTimestampBehavior.entries[ordinal]
