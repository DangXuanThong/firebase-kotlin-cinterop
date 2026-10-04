package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.Source`
 *
 * Configures the behavior of `get()` calls on [DocumentReference] and [Query]. By
 * providing a `Source` value, these methods can be configured to fetch results only from the
 * server, only from the local cache, or attempt to fetch results from the server and fall back to
 * the cache (which is the default).
 */
enum class Source {
    /**
     * Causes Cloud Firestore to try to retrieve an up-to-date (server-retrieved) snapshot, but fall
     * back to returning cached data if the server can't be reached.
     */
    DEFAULT,

    /**
     * Causes Cloud Firestore to avoid the cache, generating an error if the server cannot be reached.
     * Note that the cache will still be updated if the server request succeeds. Also note that
     * latency-compensation still takes effect, so any pending write operations will be visible in the
     * returned data (merged into the server-provided data).
     */
    SERVER,

    /**
     * Causes Cloud Firestore to immediately return a value from the cache, ignoring the server
     * completely (implying that the returned value may be stale with respect to the value on the
     * server). If there is no data in the cache to satisfy the `get()` call, [DocumentReference.get]
     * will return an error and [Query.get] will return an empty [QuerySnapshot] with no documents.
     */
    CACHE
}
