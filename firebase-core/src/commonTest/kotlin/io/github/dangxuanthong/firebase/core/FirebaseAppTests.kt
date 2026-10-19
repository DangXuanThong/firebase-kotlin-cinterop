package io.github.dangxuanthong.firebase.core

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    // ── Default / named app retrieval ───────────────────────────────────

    @Test
    fun defaultApp_getBeforeInitialize_throws() {
        assertFailsWith<IllegalStateException> { Firebase.app }
    }

    @Test
    fun namedApp_getBeforeInitialize_throws() {
        assertFailsWith<IllegalStateException> { Firebase.app("test") }
    }

    @Test
    fun defaultApp_initialize_returnsDefaultApp() {
        val app = Firebase.initialize(testContext, options)
        assertEquals(app, Firebase.app)
        assertEquals(app.name, Firebase.app.name)
        assertEquals(app.options, Firebase.app.options)
    }

    @Test
    fun namedApp_initialize_returnsNamedApp() {
        val app = Firebase.initialize(testContext, options, "test")
        assertEquals(app, Firebase.app("test"))
        assertEquals(app.name, Firebase.app("test").name)
        assertEquals(app.options, Firebase.app("test").options)
    }

    @Test
    fun defaultApp_initializeOnlyNamedApp_throws() {
        Firebase.initialize(testContext, options, "test")
        assertFailsWith<IllegalStateException> { Firebase.app }
    }

    @Test
    fun apps_initializeDefaultAndNamed_returnsBoth() {
        val default = Firebase.initialize(testContext, options)
        val named = Firebase.initialize(testContext, options, "test")
        assertEquals(setOf(default, named), Firebase.apps(testContext).toSet())
    }

    // ── Duplicate initialization ────────────────────────────────────────

    @Test
    fun defaultApp_initializeTwice_throws() {
        Firebase.initialize(testContext, options)
        assertFailsWith<IllegalStateException> { Firebase.initialize(testContext, options) }
    }

    @Test
    fun namedApp_initializeSameNameTwice_throws() {
        Firebase.initialize(testContext, options, "test")
        assertFailsWith<IllegalStateException> {
            Firebase.initialize(testContext, options, "test")
        }
    }

    // ── Options round-trip ──────────────────────────────────────────────

    @Test
    fun appOptions_initializeWithAllFields_returnsSameOptions() {
        val app = Firebase.initialize(testContext, fullOptions)
        assertEquals(fullOptions, app.options)
    }

    @Test
    fun appOptions_initializeWithRequiredOnly_unsetFieldsAreNull() {
        val app = Firebase.initialize(testContext, options)
        assertNull(app.options.databaseUrl)
        assertNull(app.options.gaTrackingId)
        assertNull(app.options.storageBucket)
        assertNull(app.options.gcmSenderId)
    }

    // ── Delete lifecycle ────────────────────────────────────────────────

    @Test
    fun app_deleteAndGetProperties_throws() {
        val app = Firebase.initialize(testContext, options)
        app.delete()
        assertFailsWith<IllegalStateException> { app.name }
        assertFailsWith<IllegalStateException> { app.options }
    }

    @Test
    fun app_deleteTwice_doesNotThrow() {
        val app = Firebase.initialize(testContext, options)
        app.delete()
        app.delete()
    }

    @Test
    fun apps_addAndDelete_returnsOnlyActive() {
        assertTrue(Firebase.apps(testContext).isEmpty())
        val app = Firebase.initialize(testContext, options)
        assertEquals(listOf(app), Firebase.apps(testContext))
        app.delete()
        assertTrue(Firebase.apps(testContext).isEmpty())
    }

    @Test
    fun defaultApp_deleteAndGet_throws() {
        val app = Firebase.initialize(testContext, options)
        app.delete()
        assertFailsWith<IllegalStateException> { Firebase.app }
    }

    @Test
    fun defaultApp_deleteAndReinitialize_succeeds() {
        val app1 = Firebase.initialize(testContext, options)
        app1.delete()
        val app2 = Firebase.initialize(testContext, options)
        assertEquals(app2, Firebase.app)
    }

    @Test
    fun namedApp_deleteAndReinitialize_succeeds() {
        val app1 = Firebase.initialize(testContext, options, "reusable")
        app1.delete()
        val app2 = Firebase.initialize(testContext, options, "reusable")
        assertEquals(app2, Firebase.app("reusable"))
    }

    // ── Firebase.options shortcut ────────────────────────────────────────

    @Test
    fun firebaseOptions_getWithDefaultApp_returnsAppOptions() {
        Firebase.initialize(testContext, options)
        assertEquals(options, Firebase.options)
    }

    @Test
    fun firebaseOptions_getWithoutDefaultApp_throws() {
        assertFailsWith<IllegalStateException> { Firebase.options }
    }

    // ── Builder-style Firebase.initialize { … } ─────────────────────────

    @Test
    fun defaultApp_initializeWithBuilder_returnsApp() {
        val app = Firebase.initialize(context = testContext) {
            applicationId = options.applicationId
            apiKey = options.apiKey
            projectId = options.projectId
        }
        assertEquals(app, Firebase.app)
        assertEquals(options, app.options)
    }

    @Test
    fun namedApp_initializeWithBuilder_returnsApp() {
        val app = Firebase.initialize(context = testContext, name = "builder-test") {
            applicationId = options.applicationId
            apiKey = options.apiKey
            projectId = options.projectId
        }
        assertEquals(app, Firebase.app("builder-test"))
    }

    @Test
    fun builder_missingApplicationId_throwsIllegalArgument() {
        assertFailsWith<IllegalArgumentException> {
            Firebase.initialize(context = testContext) {
                apiKey = "AIzaSyDummyApiKeyFromConfig123456"
                projectId = "test-project"
            }
        }
    }

    @Test
    fun builder_missingApiKey_throwsIllegalArgument() {
        assertFailsWith<IllegalArgumentException> {
            Firebase.initialize(context = testContext) {
                applicationId = "1:1234567890:android:abcdef1234567890"
                projectId = "test-project"
            }
        }
    }

    @Test
    fun builder_missingProjectId_throwsIllegalArgument() {
        assertFailsWith<IllegalArgumentException> {
            Firebase.initialize(context = testContext) {
                applicationId = "1:1234567890:android:abcdef1234567890"
                apiKey = "AIzaSyDummyApiKeyFromConfig123456"
            }
        }
    }

    @Test
    fun builder_setAllFields_roundTrips() {
        val app = Firebase.initialize(context = testContext) {
            applicationId = "1:1234567890:android:abcdef1234567890"
            apiKey = "AIzaSyDummyApiKeyFromConfig123456"
            projectId = "test-project"
            databaseUrl = "https://test.firebaseio.com"
            gaTrackingId = "UA-123456-1"
            storageBucket = "test.appspot.com"
            gcmSenderId = "1234567890"
        }
        assertEquals(fullOptions, app.options)
    }

    // ── Multiple distinct named apps ────────────────────────────────────

    @Test
    fun namedApps_initializeDistinct_coexistIndependently() {
        val app1 = Firebase.initialize(testContext, options, "first")
        val app2 = Firebase.initialize(testContext, options, "second")
        assertEquals(app1, Firebase.app("first"))
        assertEquals(app2, Firebase.app("second"))
        assertEquals("first", app1.name)
        assertEquals("second", app2.name)
    }

    @Test
    fun namedApp_deleteOne_otherUnaffected() {
        val app1 = Firebase.initialize(testContext, options, "keep")
        val app2 = Firebase.initialize(testContext, options, "remove")
        app2.delete()
        assertEquals(app1, Firebase.app("keep"))
        assertFailsWith<IllegalStateException> { Firebase.app("remove") }
    }

    // ── equals / hashCode ───────────────────────────────────────────────

    @Test
    fun app_getMultipleTimes_returnsEqual() {
        Firebase.initialize(testContext, options)
        val ref1 = Firebase.app
        val ref2 = Firebase.app
        assertEquals(ref1, ref2)
        assertEquals(ref1.hashCode(), ref2.hashCode())
    }

    @Test
    fun app_compareDifferentApps_returnsNotEqual() {
        val defaultApp = Firebase.initialize(testContext, options)
        val named = Firebase.initialize(testContext, options, "other")
        assertNotEquals(defaultApp, named)
    }

    @Test
    fun app_compareWithNull_returnsNotEqual() {
        val app = Firebase.initialize(testContext, options)
        assertNotEquals<Any?>(app, null)
    }

    // ── tearDown ────────────────────────────────────────────────────────

    @AfterTest
    fun tearDown() {
        Firebase.apps(testContext).forEach { it.delete() }
    }
}
