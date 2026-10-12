package com.dangxuanthong.firebase.firestore.mappers

import com.dangxuanthong.firebase.firestore.SnapshotMetadata
import dev.gitlive.firebase.firestore.SnapshotMetadata as RealSnapshotMetadata

fun RealSnapshotMetadata.toSnapshotMetadata() = SnapshotMetadata(
    hasPendingWrites = this.hasPendingWrites,
    isFromCache = this.isFromCache
)
