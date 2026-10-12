package com.dangxuanthong.firebase.firestore.mappers

import com.dangxuanthong.firebase.firestore.ServerTimestampBehavior
import dev.gitlive.firebase.firestore.ServerTimestampBehavior as RealServerTimestampBehavior

fun ServerTimestampBehavior.toRealServerTimestampBehavior(): RealServerTimestampBehavior =
    RealServerTimestampBehavior.entries[ordinal]
