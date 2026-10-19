# firebase-kotlin-cinterop

Kotlin Multiplatform (KMP) Firebase SDK supporting **Android** and **Linux (`linuxX64`)** via
Kotlin/Native `cinterop` and the official **Firebase C++ SDK**.

Built specifically for **Linux (`linuxX64`)** desktop and embedded applications (where official
Firebase Kotlin / Android SDKs lack native target support), while providing a unified KMP API
matching standard Kotlin Firebase libraries on Android.

---

## 🌟 Key Features & Updates

- **Direct Integration with Official Firebase C++ SDK**: Native compatibility layer (`native/`)
  built directly on Google's official [Firebase C++ SDK](https://github.com/firebase/firebase-cpp-sdk).
- **C11 / C++11 Modern Linux ABI**: Compiled with `-DFIREBASE_LINUX_USE_CXX11_ABI=ON`
  (`_GLIBCXX_USE_CXX11_ABI=1`) and `-DCMAKE_POSITION_INDEPENDENT_CODE=ON` (`-fPIC`) for seamless
  linkage with modern Linux system libraries and Kotlin/Native `cinterop`.
- **Automated SDK Build Script**: Built-in script `scripts/install_firebase_sdk.sh` automatically
  fetches, configures (App Core + Auth + Firestore), compiles, and packages the required SDK static
  archives into `native/firebase-cpp-sdk`.
- **Unified Kotlin API**:
  - **Android**: Uses native Kotlin/JVM Firebase SDK (`com.google.firebase` /
    `dev.gitlive.firebase`).
  - **Linux (`linuxX64`)**: Binds directly to `libfirebase_native.so` via Kotlin/Native `cinterop`.
- **Coroutines & Real-Time Flows**: Cold Kotlin `Flow` (`callbackFlow`) for continuous real-time
  Firestore listeners with automatic cleanup on cancellation.
- **`google-services.json` Config Parser**: Direct initialization from `google-services.json`
  config payloads.

---

## 📦 Modules

| Module                    | Description                                                                                                                                                                          | Targets               |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|
| **`:firebase-core`**      | Core Firebase initialization (`Firebase.initialize`), `FirebaseApp`, `FirebaseOptions`, and `google-services.json` config parsing.                                                   | `android`, `linuxX64` |
| **`:firebase-firestore`** | Cloud Firestore database API (`Firebase.firestore`, `DocumentReference`, `DocumentSnapshot`, `CollectionReference`, real-time `snapshots` flow, Kotlin `serializer()` data mapping). | `android`, `linuxX64` |

---

## 🛠 Repository Layout

```
├── firebase-core/                  # KMP Firebase Core module (Android & linuxX64)
├── firebase-firestore/             # KMP Cloud Firestore module (Android & linuxX64)
├── native/                         # Native C++ compatibility layer & SDK
│   ├── firebase-cpp-sdk/           # Installed Firebase C++ SDK (lib/ and include/)
│   ├── include/
│   │   └── firebase_compat.h       # C-ABI header file for cinterop
│   ├── src/
│   │   └── firebase_impl.cpp       # C++ implementation wrapping App, Auth & Firestore
│   ├── test/
│   │   └── test.c                  # Pure C test executable verifying C-ABI bridge
│   └── CMakeLists.txt              # Builds libfirebase_native.so and test executable
└── scripts/
    └── install_firebase_sdk.sh     # Automated script to fetch, build & package Firebase C++ SDK
```

---

## 🔧 Building Native C-Interop (`linuxX64`)

### 1. Build & Install Firebase C++ SDK

Run the automated build script to download and compile the official Firebase C++ SDK:

```bash
./scripts/install_firebase_sdk.sh
```

This script:

1. Clones tag `v14.0.0` from `firebase-cpp-sdk`.
2. Configures CMake with:
```
-DCMAKE_BUILD_TYPE=Release \
    -DFIREBASE_INCLUDE_LIBRARY_DEFAULT=OFF \
    -DFIREBASE_INCLUDE_AUTH=ON \
    -DFIREBASE_INCLUDE_FIRESTORE=ON \
    -DFIREBASE_LINUX_USE_CXX11_ABI=ON \
    -DCMAKE_POSITION_INDEPENDENT_CODE=ON
```
3. Compiles `firebase_firestore`, `firebase_auth`, `firebase_app`, `gRPC`, `BoringSSL`, `Protobuf`,
   and `LevelDB`.
4. Copies compiled `.a` static archives and public C++ headers into `native/firebase-cpp-sdk/`.

### 2. Build Native Compat Shared Library (`libfirebase_native.so`) & Run C Test

To build the C compatibility shared library and run the verification test:

```bash
# Configure & compile native library and C test binary
cmake -S native -B native/build
cmake --build native/build
```

---

## 🚀 Quick Start

### 1. Dependency Setup

Add the modules to your KMP project's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.dangxuanthong.firebase:firestore:0.0.2")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
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
    .document("test")
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
        .document("test")
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

## 📄 License

Apache 2.0. See [LICENSE](LICENSE).
