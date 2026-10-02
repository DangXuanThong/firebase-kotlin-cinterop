import com.dangxuanthong.firebase.core.Firebase
import com.dangxuanthong.firebase.firestore.FirebaseFirestore
import com.dangxuanthong.firebase.firestore.data
import com.dangxuanthong.firebase.firestore.exceptions.FirestoreException
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    Firebase.initialize {
        this.apiKey = "odkwodkd"
        this.projectId = "dowdowm"
        this.applicationId = "mdwodmow"
    }
    val db = FirebaseFirestore()
    try {
        val doc = db.collection("cinterop_test").document("hello").get()
        println("exists=${doc.exists}")
        println(doc.data<TestDoc>())
    } catch (e: FirestoreException) {
        println("${e::class.simpleName}: ${e.message}")
    }
    db.close()
}
