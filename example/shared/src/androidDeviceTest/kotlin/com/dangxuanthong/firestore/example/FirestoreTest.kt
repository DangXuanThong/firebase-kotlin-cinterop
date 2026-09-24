package com.dangxuanthong.firestore.example

import TestDoc
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dangxuanthong.firestore.FirestoreConfig
import com.dangxuanthong.firestore.data
import com.dangxuanthong.firestore.initializeFirestore
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirestoreTest {
    @Test
    fun readsDocument() = runTest {
        val context =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val db = initializeFirestore(
            context,
            FirestoreConfig(
                "...",
                "...",
                "..."
            )
        )
        val doc = db.collection("cinterop_test").document("hello").get()
        assert(doc.exists)
        assert(doc.data<TestDoc>().message == "hello from C++")
    }
}
