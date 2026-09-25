import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    android {
        namespace = "com.dangxuanthong.firebase.example.shared"
        compileSdk = 37
        minSdk = 24

        compilerOptions {
            jvmTarget = JvmTarget.JVM_25
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }
    }

    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.firebaseFirestore)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
        }
        named("androidDeviceTest").dependencies {
            implementation("androidx.test.ext:junit:1.3.0")
            implementation("androidx.test:runner:1.7.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
        }
    }
}
