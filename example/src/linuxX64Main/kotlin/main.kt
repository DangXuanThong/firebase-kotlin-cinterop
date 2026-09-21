import com.dangxuanthong.firestore.data
import com.dangxuanthong.firestore.initializeFirestore
import com.dangxuanthong.firestore.loadFirestoreConfig
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class TestDoc(val message: String)

fun main(): Unit = runBlocking {
    val db = initializeFirestore(loadFirestoreConfig())
    val doc = db.collection("cinterop_test").document("hello").get()
    println("exists=${doc.exists}")
    println(doc.data<TestDoc>())
}
