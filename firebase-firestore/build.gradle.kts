import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    android {
        namespace = "com.dangxuanthong.firebase.firestore.shared"
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
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    linuxX64()

    sourceSets {
        commonMain.dependencies {
            api(projects.firebaseCore)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")
        }
        linuxX64Main.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.11.0")
        }
        androidMain.dependencies {
            implementation("dev.gitlive:firebase-firestore:2.7.0")
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        optIn.addAll(
            "kotlinx.cinterop.ExperimentalForeignApi",
            "kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}
