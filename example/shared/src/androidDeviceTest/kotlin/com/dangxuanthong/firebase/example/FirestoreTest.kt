package com.dangxuanthong.firebase.example

import TestDoc
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dangxuanthong.firebase.core.Firebase
import com.dangxuanthong.firebase.core.initialize
import com.dangxuanthong.firebase.core.parseGoogleServicesConfig
import com.dangxuanthong.firebase.firestore.data
import com.dangxuanthong.firebase.firestore.firestore
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirestoreTest {
    @Test
    fun readsDocument() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val jsonText = context.assets.open("google-services.json").bufferedReader().readText()
        val config = parseGoogleServicesConfig(jsonText)

        val db = Firebase.initialize(context, config).firestore
        val doc = db.collection("cinterop_test").document("hello").get()
        assert(doc.exists)
        assert(doc.data<TestDoc>().message == "hello from C++")
    }
}
