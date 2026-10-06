import com.dangxuanthong.firebase.core.Firebase
import com.dangxuanthong.firebase.firestore.FirebaseFirestore
import com.dangxuanthong.firebase.firestore.data
import com.dangxuanthong.firebase.firestore.exceptions.FirestoreException
import com.dangxuanthong.firebase.firestore.set
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    Firebase.initialize(path = "google-services.json")
    val db = FirebaseFirestore()
    try {
        val doc = db.collection("cinterop_test").document("test2")
            .apply {
                set(data = TestDoc("hello from kotlin/native"))
            }.get()
        println("exists=${doc.exists}")
        println(doc.data<TestDoc>())
    } catch (e: FirestoreException) {
        println("${e::class.simpleName}: ${e.message}")
    }
    db.close()
}
