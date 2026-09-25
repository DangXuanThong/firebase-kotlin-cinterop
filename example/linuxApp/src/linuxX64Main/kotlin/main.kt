import com.dangxuanthong.firebase.core.Firebase
import com.dangxuanthong.firebase.core.initialize
import com.dangxuanthong.firebase.firestore.FirebaseFirestore
import com.dangxuanthong.firebase.firestore.data
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    Firebase.initialize(path = "google-services.json")
    val db = FirebaseFirestore()
    val doc = db.collection("cinterop_test").document("hello").get()
    println("exists=${doc.exists}")
    println(doc.data<TestDoc>())
    db.close()
}
