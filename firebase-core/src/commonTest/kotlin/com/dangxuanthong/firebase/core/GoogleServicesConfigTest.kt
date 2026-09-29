package com.dangxuanthong.firebase.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private const val MULTI_APP_JSON = """
{
  "project_info": {
    "project_id": "my-project",
    "storage_bucket": "my-project.appspot.com"
  },
  "client": [
    {
      "client_info": { "mobilesdk_app_id": "1:111:android:aaa" },
      "api_key": [ { "current_key": "android-key" } ]
    },
    {
      "client_info": { "mobilesdk_app_id": "1:111:ios:bbb" },
      "api_key": [ { "current_key": "ios-key" } ]
    }
  ]
}
"""

class GoogleServicesConfigTest {

    @Test
    fun defaultsToFirstClientWhenNoApplicationIdGiven() {
        val options = parseGoogleServicesConfig(MULTI_APP_JSON)
        assertEquals("1:111:android:aaa", options.applicationId)
        assertEquals("android-key", options.apiKey)
    }

    @Test
    fun selectsClientMatchingApplicationId() {
        val options = parseGoogleServicesConfig(MULTI_APP_JSON, applicationId = "1:111:ios:bbb")
        assertEquals("1:111:ios:bbb", options.applicationId)
        assertEquals("ios-key", options.apiKey)
    }

    @Test
    fun throwsWhenApplicationIdNotFound() {
        assertFailsWith<IllegalStateException> {
            parseGoogleServicesConfig(MULTI_APP_JSON, applicationId = "does-not-exist")
        }
    }

    @Test
    fun throwsWhenProjectHasNoApiKey() {
        val noKeyJson = """
        {
          "project_info": { "project_id": "p", "storage_bucket": null },
          "client": [ { "client_info": { "mobilesdk_app_id": "1:1:a:a" }, "api_key": [] } ]
        }
        """.trimIndent()
        assertFailsWith<IllegalStateException> { parseGoogleServicesConfig(noKeyJson) }
    }
}
