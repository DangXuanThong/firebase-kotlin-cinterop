import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
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

    linuxX64 {
        compilerOptions {
            optIn.addAll(
                "kotlinx.cinterop.ExperimentalForeignApi",
                "kotlinx.serialization.ExperimentalSerializationApi"
            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.firebaseCore)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        linuxX64Main.dependencies {
            implementation(libs.kotlinx.serialization.cbor)
        }
        androidMain.dependencies {
            implementation(libs.gitlive.firebase.firestore)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
