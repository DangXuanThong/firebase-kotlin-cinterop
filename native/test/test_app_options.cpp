#include "test_helpers.h"
#include "firebase_compat.h"

static const char* VALID_GOOGLE_SERVICES_JSON = R"({
  "project_info": {
    "project_number": "1234567890",
    "firebase_url": "https://test-project.firebaseio.com",
    "project_id": "test-project",
    "storage_bucket": "test-project.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:1234567890:android:abcdef1234567890",
        "android_client_info": {
          "package_name": "com.example.test"
        }
      },
      "oauth_client": [
        {
          "client_id": "1234567890-abcdef.apps.googleusercontent.com",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "AIzaSyDummyApiKeyFromConfig123456"
        }
      ]
    }
  ],
  "configuration_version": "1"
})";

static void AppOptions_create_validArguments_returnsNonNullPointer() {
  std::cout << "\n=== Test: AppOptions_create_validArguments_returnsNonNullPointer ===\n";
  NativeAppOptions* opts = AppOptions_create(
      "1:1234567890:web:a1b2c3d4e5f6",
      "AIzaSyDummyApiKeyForLocalTest",
      "1234567890",
      "https://test.firebaseio.com",
      "UA-123456-1",
      "test.appspot.com",
      "test-project"
  );
  ASSERT_NOT_NULL(opts, "AppOptions_create returns non-null pointer with valid arguments")

  AppOptions_destroy(opts);
}

static void AppOptions_create_nullAndEmptyArguments_handlesSafely() {
  std::cout << "\n=== Test: AppOptions_create_nullAndEmptyArguments_handlesSafely ===\n";

  NativeAppOptions* opts_nulls = AppOptions_create(nullptr, nullptr, nullptr, nullptr, nullptr, nullptr, nullptr);
  ASSERT_NOT_NULL(opts_nulls, "AppOptions_create handles all nullptr arguments safely")
  AppOptions_destroy(opts_nulls);

  NativeAppOptions* opts_empty = AppOptions_create("", "", "", "", "", "", "");
  ASSERT_NOT_NULL(opts_empty, "AppOptions_create handles empty string \"\" arguments safely")
  AppOptions_destroy(opts_empty);

  NativeAppOptions* opts_partial = AppOptions_create(
      "1:1234567890:web:a1b2c3d4e5f6",
      "AIzaSyDummyApiKeyForLocalTest",
      nullptr, nullptr, nullptr, nullptr,
      "test-project"
  );
  ASSERT_NOT_NULL(opts_partial, "AppOptions_create handles partial nullptr optional parameters")

  AppOptions_destroy(opts_partial);
}

static void AppOptions_fromJson_validJsonString_returnsNonNullPointer() {
  std::cout << "\n=== Test: AppOptions_fromJson_validJsonString_returnsNonNullPointer ===\n";
  NativeAppOptions* opts = AppOptions_fromJson(VALID_GOOGLE_SERVICES_JSON);
  ASSERT_NOT_NULL(opts, "AppOptions_fromJson parses valid google-services.json successfully")

  AppOptions_destroy(opts);
}

static void AppOptions_fromJson_invalidJson_returnsNullPointer() {
  std::cout << "\n=== Test: AppOptions_fromJson_invalidJson_returnsNullPointer ===\n";

  NativeAppOptions* opts_invalid = AppOptions_fromJson("{ invalid_json_syntax ");
  ASSERT_NULL(opts_invalid, "AppOptions_fromJson fails gracefully (returns nullptr) on malformed JSON")

  NativeAppOptions* opts_empty_json = AppOptions_fromJson("{}");
  ASSERT_NULL(opts_empty_json, "AppOptions_fromJson fails gracefully on empty JSON object {}")
}

static void AppOptions_fromJson_nullAndEmptyInput_returnsNullPointer() {
  std::cout << "\n=== Test: AppOptions_fromJson_nullAndEmptyInput_returnsNullPointer ===\n";

  NativeAppOptions* opts_null = AppOptions_fromJson(nullptr);
  ASSERT_NULL(opts_null, "AppOptions_fromJson handles nullptr safely (returns nullptr)")

  NativeAppOptions* opts_empty_str = AppOptions_fromJson("");
  ASSERT_NULL(opts_empty_str, "AppOptions_fromJson handles empty string \"\" safely (returns nullptr)")
}

static void AppOptions_destroy_nullAndValidPointers_executesSafely() {
  std::cout << "\n=== Test: AppOptions_destroy_nullAndValidPointers_executesSafely ===\n";

  AppOptions_destroy(nullptr);
  ASSERT_TRUE(true, "AppOptions_destroy(nullptr) executes safely without crashing")

  NativeAppOptions* opts = AppOptions_fromJson(VALID_GOOGLE_SERVICES_JSON);
  AppOptions_destroy(opts);
  ASSERT_TRUE(true, "AppOptions_destroy(valid_ptr) executes cleanly")
}

void run_app_options_tests() {
  AppOptions_create_validArguments_returnsNonNullPointer();
  AppOptions_create_nullAndEmptyArguments_handlesSafely();
  AppOptions_fromJson_validJsonString_returnsNonNullPointer();
  AppOptions_fromJson_invalidJson_returnsNullPointer();
  AppOptions_fromJson_nullAndEmptyInput_returnsNullPointer();
  AppOptions_destroy_nullAndValidPointers_executesSafely();
}
