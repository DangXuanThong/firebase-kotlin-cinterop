import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.firestore.data
import io.github.dangxuanthong.firebase.firestore.exceptions.FirestoreException
import io.github.dangxuanthong.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    Firebase.initialize(path = "google-services.json")
    val db = Firebase.firestore
    try {
        db.collection("cinterop_test").document("test1").snapshots
            .collect {
                println(it.data<TestDoc>())
            }
    } catch (e: FirestoreException) {
        println("${e::class.simpleName}: ${e.message}")
    }
    db.terminate()
}
