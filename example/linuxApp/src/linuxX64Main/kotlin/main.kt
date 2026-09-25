import com.dangxuanthong.firestore.Firebase
import com.dangxuanthong.firestore.data
import com.dangxuanthong.firestore.initialize
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val db = Firebase.initialize(path = "google-services.json")
    val doc = db.collection("cinterop_test").document("hello").get()
    println("exists=${doc.exists}")
    println(doc.data<TestDoc>())
    db.close()
}
