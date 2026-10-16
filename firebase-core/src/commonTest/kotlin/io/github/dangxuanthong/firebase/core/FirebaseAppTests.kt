package io.github.dangxuanthong.firebase.core

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

class FirebaseAppTests {
    private val options = FirebaseOptions(
        applicationId = "1:1234567890:android:abcdef1234567890",
        apiKey = "AIzaSyDummyApiKeyFromConfig123456",
        projectId = "test-project"
    )
    private val fullOptions = FirebaseOptions(
        applicationId = "1:1234567890:android:abcdef1234567890",
        apiKey = "AIzaSyDummyApiKeyFromConfig123456",
        projectId = "test-project",
        databaseUrl = "https://test.firebaseio.com",
        gaTrackingId = "UA-123456-1",
        storageBucket = "test.appspot.com",
        gcmSenderId = "1234567890"
    )

    @Test
    fun firebaseApp_notInitializeDefaultApp_getDefaultAppThrow() {
        assertFailsWith<IllegalStateException> { Firebase.app }
    }

    @Test
    fun firebaseApp_notInitializeNamedApp_getNamedAppThrow() {
        assertFailsWith<IllegalStateException> { Firebase.app("test") }
    }

    @Test
    fun firebaseApp_initializeDefaultApp_returnsDefaultApp() {
        val app = Firebase.initialize(testContext, options)
        assertEquals(app, Firebase.app)
        assertEquals(app.name, Firebase.app.name)
        assertEquals(app.options, Firebase.app.options)
    }

    @Test
    fun firebaseApp_initializeNamedApp_returnsNamedApp() {
        val app = Firebase.initialize(testContext, options, "test")
        assertEquals(app, Firebase.app("test"))
        assertEquals(app.name, Firebase.app("test").name)
        assertEquals(app.options, Firebase.app("test").options)
    }

    @Test
    fun firebaseApp_initializeNamedApp_doesNotCreateDefaultApp() {
        Firebase.initialize(testContext, options, "test")
        assertFailsWith<IllegalStateException> { Firebase.app }
    }

    @Test
    fun firebaseApp_initializeDefaultAndNamedApps_coexist() {
        val default = Firebase.initialize(testContext, options)
        val named = Firebase.initialize(testContext, options, "test")
        assertEquals(setOf(default, named), Firebase.apps(testContext).toSet())
    }

    @Test
    fun firebaseApp_initializeDefaultAppMultipleTimes_throw() {
        Firebase.initialize(testContext, options)
        assertFailsWith<IllegalStateException> { Firebase.initialize(testContext, options) }
    }

    @Test
    fun firebaseApp_initializeNamedAppMultipleTimes_throw() {
        Firebase.initialize(testContext, options, "test")
        assertFailsWith<IllegalStateException> {
            Firebase.initialize(testContext, options, "test")
        }
    }

    @Test
    fun firebaseApp_optionsRoundTrip_allFieldsSet() {
        val app = Firebase.initialize(testContext, fullOptions)
        assertEquals(fullOptions, app.options)
    }

    @Test
    fun firebaseApp_optionsRoundTrip_unsetFieldsAreNull() {
        val app = Firebase.initialize(testContext, options)
        assertNull(app.options.databaseUrl)
        assertNull(app.options.gaTrackingId)
        assertNull(app.options.storageBucket)
        assertNull(app.options.gcmSenderId)
    }

    @Test
    fun firebaseApp_deleteApp_getPropertiesThrow() = runTest {
        val app = Firebase.initialize(testContext, options)
        app.delete()
        assertFailsWith<IllegalStateException> { app.name }
        assertFailsWith<IllegalStateException> { app.options }
    }

    @Test
    fun firebaseApp_deleteTwice_doesNotThrow() = runTest {
        val app = Firebase.initialize(testContext, options)
        app.delete()
        app.delete()
    }

    @Test
    fun firebaseApp_getApps_returnUndeletedApps() = runTest {
        assertTrue(Firebase.apps(testContext).isEmpty())
        val app = Firebase.initialize(testContext, options)
        assertEquals(listOf(app), Firebase.apps(testContext))
        app.delete()
        assertTrue(Firebase.apps(testContext).isEmpty())
    }

    @AfterTest
    fun tearDown() = runBlocking {
        Firebase.apps(testContext).forEach { it.delete() }
    }
}
