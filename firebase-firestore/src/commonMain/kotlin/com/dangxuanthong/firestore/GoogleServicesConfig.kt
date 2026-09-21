package com.dangxuanthong.firestore

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath

@Serializable
private data class GoogleServicesJson(val project_info: ProjectInfo, val client: List<Client>) {
    @Serializable data class ProjectInfo(val project_id: String)

    @Serializable data class Client(val client_info: ClientInfo, val api_key: List<ApiKey>)

    @Serializable data class ClientInfo(val mobilesdk_app_id: String)

    @Serializable data class ApiKey(val current_key: String)
}

/**
 * Parses google-services.json content into a [FirestoreConfig]. Not needed
 * on Android — the Google Services Gradle plugin auto-configures that
 * platform already. Useful on JVM and native, where nothing does.
 */
fun parseGoogleServicesConfig(jsonText: String): FirestoreConfig {
    val parsed = Json { ignoreUnknownKeys = true }
        .decodeFromString<GoogleServicesJson>(jsonText)
    val client = parsed.client.firstOrNull()
        ?: error("google-services.json has no client entries")
    return FirestoreConfig(
        apiKey = client.api_key.firstOrNull()?.current_key
            ?: error("google-services.json has no api_key"),
        appId = client.client_info.mobilesdk_app_id,
        projectId = parsed.project_info.project_id
    )
}

fun loadFirestoreConfig(path: String = "google-services.json"): FirestoreConfig {
    val jsonText = FileSystem.SYSTEM.read(path.toPath()) { readUtf8() }
    return parseGoogleServicesConfig(jsonText)
}
