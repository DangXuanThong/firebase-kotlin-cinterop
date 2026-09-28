package com.dangxuanthong.firebase.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json by lazy {
    Json { ignoreUnknownKeys = true }
}

/**
 * Parses google-services.json content into a [FirebaseOptions].
 *
 * A google-services.json lists one `client` entry per app registered in the project. By default
 * the first entry is used; pass [applicationId] (the `mobilesdk_app_id` of the app you want,
 * e.g. `1:1234567890:android:abcdef`) to pick a specific one in multi-app projects.
 *
 * @throws IllegalStateException if the file has no matching client entry or no API key.
 */
fun parseGoogleServicesConfig(jsonText: String, applicationId: String? = null): FirebaseOptions {
    val googleServicesJson = json.decodeFromString<GoogleServicesJson>(jsonText)
    val client = if (applicationId == null) {
        googleServicesJson.client.firstOrNull()
            ?: error("google-services.json has no client entries")
    } else {
        googleServicesJson.client.firstOrNull { it.clientInfo.mobileSdkAppId == applicationId }
            ?: error("google-services.json has no client with applicationId $applicationId")
    }

    return FirebaseOptions(
        applicationId = client.clientInfo.mobileSdkAppId,
        apiKey = client.apiKey.firstOrNull()?.currentKey
            ?: error("google-services.json has no API key"),
        storageBucket = googleServicesJson.projectInfo.storageBucket,
        projectId = googleServicesJson.projectInfo.projectId
    )
}

@Serializable
private data class GoogleServicesJson(
    @SerialName("project_info") val projectInfo: ProjectInfo,
    val client: List<Client>
) {

    @Serializable
    data class ProjectInfo(
        @SerialName("project_id") val projectId: String,
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
