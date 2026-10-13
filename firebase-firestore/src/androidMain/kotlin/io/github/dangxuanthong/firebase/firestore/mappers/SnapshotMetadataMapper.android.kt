package io.github.dangxuanthong.firebase.firestore.mappers

import dev.gitlive.firebase.firestore.SnapshotMetadata as RealSnapshotMetadata
import io.github.dangxuanthong.firebase.firestore.SnapshotMetadata

fun RealSnapshotMetadata.toSnapshotMetadata() = SnapshotMetadata(
    hasPendingWrites = this.hasPendingWrites,
    isFromCache = this.isFromCache
)
