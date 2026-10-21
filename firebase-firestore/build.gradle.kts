import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    android {
        namespace = "io.github.dangxuanthong.firebase.firestore.shared"
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
        androidMain.dependencies {
            implementation(libs.gitlive.firebase.firestore)
        }
        linuxX64Main.dependencies {
            implementation(libs.kotlinx.serialization.cbor)
            implementation(libs.kotlinx.atomicfu)
        }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        optIn.add("io.github.dangxuanthong.firebase.core.annotations.InternalFirebaseApi")
    }
}

mavenPublishing {
    coordinates(artifactId = "firestore")
}
