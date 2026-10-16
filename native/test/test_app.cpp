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

static void App_create_validOptionsAndNullName_returnsDefaultApp() {
  std::cout << "\n=== Test: App_create_validOptionsAndNullName_returnsDefaultApp ===\n";
  NativeAppOptions* opts = AppOptions_fromJson(VALID_GOOGLE_SERVICES_JSON);

  NativeApp* app = App_create(opts, nullptr);
  ASSERT_NOT_NULL(app, "App_create with NULL name creates default App successfully")

  App_destroy(app);
  AppOptions_destroy(opts);
}

static void App_create_validOptionsAndCustomName_returnsNamedApp() {
  std::cout << "\n=== Test: App_create_validOptionsAndCustomName_returnsNamedApp ===\n";
  NativeAppOptions* opts = AppOptions_fromJson(VALID_GOOGLE_SERVICES_JSON);

  NativeApp* app = App_create(opts, "SecondaryApp");
  ASSERT_NOT_NULL(app, "App_create with custom name creates named App successfully")

  App_destroy(app);
  AppOptions_destroy(opts);
}

static void App_create_nullOptions_returnsNullPointer() {
  std::cout << "\n=== Test: App_create_nullOptions_returnsNullPointer ===\n";
  NativeApp* app = App_create(nullptr, "TestApp");
  ASSERT_NULL(app, "App_create handles nullptr options safely (returns nullptr)")
}

static void App_destroy_nullAndValidPointers_executesSafely() {
  std::cout << "\n=== Test: App_destroy_nullAndValidPointers_executesSafely ===\n";
  App_destroy(nullptr);
  ASSERT_TRUE(true, "App_destroy(nullptr) executes safely without crashing")

  NativeAppOptions* opts = AppOptions_fromJson(VALID_GOOGLE_SERVICES_JSON);
  NativeApp* app = App_create(opts, "DestroyTestApp");
  App_destroy(app);
  ASSERT_TRUE(true, "App_destroy(valid_ptr) executes cleanly")

  AppOptions_destroy(opts);
}

void run_app_tests() {
  App_create_validOptionsAndNullName_returnsDefaultApp();
  App_create_validOptionsAndCustomName_returnsNamedApp();
  App_create_nullOptions_returnsNullPointer();
  App_destroy_nullAndValidPointers_executesSafely();
}
