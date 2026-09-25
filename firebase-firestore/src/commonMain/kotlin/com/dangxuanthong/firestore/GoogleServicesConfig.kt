package com.dangxuanthong.firestore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json by lazy {
    Json { ignoreUnknownKeys = true }
}

/**
 * Parses google-services.json content into a [FirestoreConfig]
 */
fun parseGoogleServicesConfig(jsonText: String): FirestoreConfig {
    val googleServicesJson = json.decodeFromString<GoogleServicesJson>(jsonText)
    val client = googleServicesJson.client.firstOrNull()
        ?: error("google-services.json has no client entries")

    return FirestoreConfig(
        applicationId = client.clientInfo.mobileSdkAppId,
        apiKey = client.apiKey.firstOrNull()?.currentKey
            ?: error("google-services.json has no API key"),
        databaseUrl = null,
        gaTrackingId = null,
        storageBucket = googleServicesJson.projectInfo.storageBucket,
        projectId = googleServicesJson.projectInfo.projectId,
        gcmSenderId = null,
        authDomain = null
    )
}

@Serializable
private data class GoogleServicesJson(
    @SerialName("project_info") val projectInfo: ProjectInfo,
    val client: List<Client>
) {

    @Serializable
    data class ProjectInfo(
        @SerialName("project_id") val projectId: String?,
        @SerialName("storage_bucket") val storageBucket: String?
    )

    @Serializable
    data class Client(
        @SerialName("client_info") val clientInfo: ClientInfo,
        @SerialName("api_key") val apiKey: List<ApiKey>
    )

    @Serializable
    data class ClientInfo(@SerialName("mobilesdk_app_id") val mobileSdkAppId: String)

    @Serializable
    data class ApiKey(@SerialName("current_key") val currentKey: String)
}
