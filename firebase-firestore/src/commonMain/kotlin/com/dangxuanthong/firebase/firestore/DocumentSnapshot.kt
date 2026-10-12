package com.dangxuanthong.firebase.firestore

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer

expect class DocumentSnapshot {
    val id: String
    val exists: Boolean
    val reference: DocumentReference
    val metadata: SnapshotMetadata

    //    fun contains(field: String): Boolean = nativeWrapper.contains(field)
//    fun contains(fieldPath: FieldPath): Boolean = nativeWrapper.contains(fieldPath.encoded)
//
//    inline fun <reified T> get(
//        field: String,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE,
//        buildSettings: DecodeSettings.Builder.() -> Unit = {}
//    ): T = decode(value = getEncoded(field, serverTimestampBehavior), buildSettings)
//
//    inline fun <T> get(
//        field: String,
//        strategy: DeserializationStrategy<T>,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE,
//        buildSettings: DecodeSettings.Builder.() -> Unit = {}
//    ): T = decode(strategy, getEncoded(field, serverTimestampBehavior), buildSettings)
//
//    @PublishedApi
//    internal fun getEncoded(
//        field: String,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
//    ): Any? = nativeWrapper.getEncoded(field, serverTimestampBehavior)
//
//    inline fun <reified T> get(
//        fieldPath: FieldPath,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE,
//        buildSettings: DecodeSettings.Builder.() -> Unit = {}
//    ): T = decode(value = getEncoded(fieldPath, serverTimestampBehavior), buildSettings)
//
//    inline fun <T> get(
//        fieldPath: FieldPath,
//        strategy: DeserializationStrategy<T>,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE,
//        buildSettings: DecodeSettings.Builder.() -> Unit = {}
//    ): T = decode(strategy, getEncoded(fieldPath, serverTimestampBehavior), buildSettings)
//
//    @PublishedApi
//    internal fun getEncoded(
//        fieldPath: FieldPath,
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
//    ): Any? = nativeWrapper.getEncoded(fieldPath.encoded, serverTimestampBehavior)
//
//    inline fun <reified T> data(
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE,
//        buildSettings: DecodeSettings.Builder.() -> Unit = {}
//    ): T = decode(encodedData(serverTimestampBehavior), buildSettings)
//
    fun <T> data(
        strategy: DeserializationStrategy<T>,
        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
    ): T
//
//    @PublishedApi
//    internal fun encodedData(
//        serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
//    ): Any? =
//        nativeWrapper.encodedData(serverTimestampBehavior)
}

inline fun <reified T : Any> DocumentSnapshot.data(
    serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
) = data(serializer<T>(), serverTimestampBehavior)
