# firebase-kotlin-cinterop

Kotlin Multiplatform (KMP) Firebase SDK supporting **Android** and **Linux (`linuxX64`)** via Kotlin/Native `cinterop` and the Firebase C++ SDK.

Built specifically for **Linux (`linuxX64`)** desktop and embedded applications (where official Firebase Kotlin / Android SDKs lack native target support), while providing a unified KMP API matching standard Kotlin Firebase libraries on Android.

---

## 🌟 Features

- **Kotlin Multiplatform (KMP)** API mirroring standard KMP Firebase libraries (`dev.gitlive.firebase`).
- **Target Platforms**:
  - **Android**: Delegates to `dev.gitlive.firebase` (GitLive Firebase SDK for Kotlin).
  - **Linux (`linuxX64`)**: Binds directly to the native `libfirebase_ffi.so` C-shared library (built on the official Firebase C++ SDK) via Kotlin/Native `cinterop`.
- **CBOR Binary Serialization**: Uses `kotlinx.serialization` (JSON / CBOR) for high-performance payload parsing on native Linux.
- **Coroutines & Real-Time Flows**: 
  - `suspendNativeCall`: Async coroutine suspension for single operations.
  - `flowNativeCall`: Cold Kotlin `Flow` (`callbackFlow`) for continuous real-time Firestore listeners with automatic cleanup/unlistening on cancellation.
- **`google-services.json` Config Parser**: Built-in `parseGoogleServicesConfig` to initialize Firebase directly from standard `google-services.json` configuration files without manual credential wiring.

---

## 📦 Modules

| Module | Description | Targets |
| --- | --- | --- |
| **`:firebase-core`** | Core Firebase initialization (`Firebase.initialize`), `FirebaseApp`, `FirebaseOptions`, and `google-services.json` config parsing. | `android`, `linuxX64` |
| **`:firebase-firestore`** | Cloud Firestore database API (`Firebase.firestore`, `DocumentReference`, `DocumentSnapshot`, `CollectionReference`, real-time `snapshots` flow, Kotlin `serializer()` data mapping). | `android`, `linuxX64` |

---

## 🚀 Quick Start

### 1. Dependency Setup

Add the modules to your KMP project's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.firebaseCore)
            implementation(projects.firebaseFirestore)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
```

### 2. Initializing Firebase

On **Linux (`linuxX64`)**, you can initialize Firebase directly from a `google-services.json` file:

```kotlin
import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.initialize
import io.github.dangxuanthong.firebase.firestore.firestore

fun main() {
    // Initialize from google-services.json
    Firebase.initialize(path = "google-services.json")

    val db = Firebase.firestore
    // ...
}
```

On **Android**, pass your application `Context` and configuration:

```kotlin
import io.github.dangxuanthong.firebase.core.Firebase
import io.github.dangxuanthong.firebase.core.initialize
import io.github.dangxuanthong.firebase.core.parseGoogleServicesConfig
import io.github.dangxuanthong.firebase.firestore.firestore

val config = parseGoogleServicesConfig(jsonText)
Firebase.initialize(context, config)

val db = Firebase.firestore
```

---

## 📖 Usage Examples

### Data Model

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class TestDoc(val message: String)
```

### Document Fetching (`suspend`)

```kotlin
val docSnapshot = Firebase.firestore
    .collection("cinterop_test")
    .document("test1")
    .get(Source.SERVER)

if (docSnapshot.exists) {
    val data: TestDoc = docSnapshot.data()
    println("Message: ${data.message}")
}
```

### Real-Time Document Snapshots (`Flow`)

```kotlin
import io.github.dangxuanthong.firebase.firestore.data
import io.github.dangxuanthong.firebase.firestore.firestore

suspend fun observeDocument() {
    val db = Firebase.firestore

    db.collection("cinterop_test")
        .document("test1")
        .snapshots
        .collect { snapshot ->
            if (snapshot.exists) {
                val doc = snapshot.data<TestDoc>()
                println("Document updated: ${doc.message}")
            }
        }
}
```

---

## 🛠 Repository Layout

```
├── firebase-core/          # KMP Firebase Core module (Android & linuxX64)
├── firebase-firestore/     # KMP Cloud Firestore module (Android & linuxX64)
│   └── src/linuxX64Main/   # cinterop bindings (NativeCall.kt, CBOR decoding)
├── packages/firebase_ffi/  # Native C/C++ FFI library (Firebase C++ SDK wrapper)
└── example/
    ├── shared/             # KMP shared example & Android device tests
    └── linuxApp/           # Executable Linux app (linuxX64)
```

---

## 🔧 Building Native C-Interop (`linuxX64`)

The `linuxX64` target links against `libfirebase_ffi.so` built from `packages/firebase_ffi/native`.

Before building the `linuxX64` target in Gradle, ensure `libfirebase_ffi.so` exists in `packages/firebase_ffi/native/build/`. The Gradle task `cinteropFdbLinuxX64` automatically generates `fdb.def` and configures the linker flags (`-lfirebase_ffi`).

---

## ⚠️ Known Issues

- **Relative `google-services.json` File Resolution on Linux**: When using `Firebase.initialize(path = "google-services.json")` or `parseGoogleServicesConfig()`, the relative file path is resolved against the process's current working directory (where the terminal command/executable binary is launched from). If `google-services.json` is missing in that working directory, Okio will throw an `okio.FileNotFoundException`. To avoid this, either pass an absolute file path or ensure the working directory contains `google-services.json`. *(May be addressed in a future update).*

---

## 🙏 Acknowledgements & Credits

This project builds upon and integrates work from the following open-source projects:

- **Joel Winarske** ([@jwinarske](https://github.com/jwinarske)): Author of the original [`firebase_ffi`](https://github.com/jwinarske/firebase_ffi) C/C++ FFI bridge layer (`packages/firebase_ffi/native`) wrapping the Firebase C++ SDK on Embedded Linux.
- **GitLive** ([`GitLiveApp/firebase-kotlin-sdk`](https://github.com/GitLiveApp/firebase-kotlin-sdk)): Authors of the Firebase Kotlin SDK (`dev.gitlive.firebase`), which powers the Android target implementation and serves as the API design model for this library.

---

## 📄 License

Apache 2.0. See [LICENSE](LICENSE).
