package com.dangxuanthong.firebase.firestore

/**
 * Mirroring `dev.gitlive.firebase.firestore.ServerTimestampBehavior`
 *
 * Controls the return value for server timestamps that have not yet been set to their final value.
 */
enum class ServerTimestampBehavior {
    /**
     * Return `null` for [FieldValue.serverTimestamp] that have not yet been set to their final value.
     */
    NONE,

    /**
     * Return local estimates for [FieldValue.serverTimestamp] that have not yet been set to their
     * final value. This estimate will likely differ from the final value and may cause
     * these pending values to change once the server result becomes available.
     */
    ESTIMATE,

    /**
     * Return the previous value for [FieldValue.serverTimestamp] that have not yet been set
     * to their final value.
     */
    PREVIOUS;

    companion object {
        val DEFAULT = NONE
    }
}
