@file:OptIn(ExperimentalAtomicApi::class)

import com.dangxuanthong.firestore.FirestoreConfig
import com.dangxuanthong.firestore.data
import com.dangxuanthong.firestore.initializeFirestore
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class TestDoc(val message: String)

fun main() = runBlocking {
    val db = initializeFirestore(
        FirestoreConfig(
            apiKey = "YOUR_API_KEY",
            appId = "YOUR_APP_ID",
            projectId = "YOUR_PROJECT_ID"
        )
    )

    val doc = db.collection("cinterop_test").document("hello").get()
    println("exists=${doc.exists}")
    println(doc.data<TestDoc>())
}
