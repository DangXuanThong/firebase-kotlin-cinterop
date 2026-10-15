#include <stdio.h>
#include <stdlib.h>
#include "firebase_compat.h"

int main() {
    printf("==> Testing C Compat Layer for Firebase AppOptions...\n");

    // 1. Test appOptions_create
    NativeAppOptions* opts1 = appOptions_create(
        "1:1234567890:web:a1b2c3d4e5f6", // app_id
        "AIzaSyDummyApiKeyForLocalTest", // api_key
        "1234567890",                    // fcm_sender_id
        "https://test.firebaseio.com",   // database_url
        "UA-123456-1",                   // ga_tracking_id
        "test.appspot.com",              // storage_bucket
        "test-project"                   // project_id
    );

    if (opts1) {
        printf("✅ appOptions_create succeeded!\n");
        appOptions_destroy(opts1);
    } else {
        printf("❌ appOptions_create failed!\n");
        return 1;
    }

    // 2. Test appOptions_from_json
    const char* mock_json = "{\n"
        "  \"project_info\": {\n"
        "    \"project_number\": \"1234567890\",\n"
        "    \"firebase_url\": \"https://test-project.firebaseio.com\",\n"
        "    \"project_id\": \"test-project\",\n"
        "    \"storage_bucket\": \"test-project.appspot.com\"\n"
        "  },\n"
        "  \"client\": [\n"
        "    {\n"
        "      \"client_info\": {\n"
        "        \"mobilesdk_app_id\": \"1:1234567890:android:abcdef1234567890\",\n"
        "        \"android_client_info\": {\n"
        "          \"package_name\": \"com.example.test\"\n"
        "        }\n"
        "      },\n"
        "      \"oauth_client\": [\n"
        "        {\n"
        "          \"client_id\": \"1234567890-abcdef.apps.googleusercontent.com\",\n"
        "          \"client_type\": 3\n"
        "        }\n"
        "      ],\n"
        "      \"api_key\": [\n"
        "        {\n"
        "          \"current_key\": \"AIzaSyDummyApiKeyFromConfig123456\"\n"
        "        }\n"
        "      ]\n"
        "    }\n"
        "  ],\n"
        "  \"configuration_version\": \"1\"\n"
        "}";

    NativeAppOptions* opts2 = appOptions_from_json(mock_json);
    if (opts2) {
        printf("✅ appOptions_from_json succeeded!\n");
        appOptions_destroy(opts2);
    } else {
        printf("❌ appOptions_from_json failed!\n");
        return 1;
    }

    printf("🎉 All C Compat Layer tests passed successfully!\n");
    return 0;
}
