package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.SnapshotMetadata`
 *
 * Metadata about a snapshot, describing the state of the snapshot.
 *
 * **Subclassing Note**: Cloud Firestore classes are not meant to be subclassed except for use
 * in test mocks. Subclassing is not supported in production code and new SDK releases may break
 * code that does so.
 */
data class SnapshotMetadata(
    /**
     * True if the snapshot contains the result of local writes (for example, `set()` or
     * `update()` calls) that have not yet been committed to the backend. If your listener
     * has opted into metadata updates (via [MetadataChanges.INCLUDE]) you will receive
     * another snapshot with `hasPendingWrites()` equal to false once the writes have been
     * committed to the backend.
     */
    val hasPendingWrites: Boolean,
    /**
     * True if the snapshot was created from cached data rather than guaranteed up-to-date
     * server data. If your listener has opted into metadata updates (via [MetadataChanges.INCLUDE]) you will receive another snapshot with `isFromCache()`
     * equal to false once the client has received up-to-date data from the backend.
     */
    val isFromCache: Boolean
)
