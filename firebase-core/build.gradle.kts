import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    android {
        namespace = "com.dangxuanthong.firebase.core.shared"
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
        compilations["main"].cinterops {
            create("fdb") {
                defFile("$projectDir/src/nativeInterop/cinterop/fdb.def")
                packageName("fdb")
                includeDirs("$projectDir/../packages/firebase_ffi/native/include")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
        }
        androidMain.dependencies {
            implementation(libs.gitlive.firebase.app)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        optIn.addAll(
            "kotlinx.cinterop.ExperimentalForeignApi" // Required for using cinterop features
        )
    }
}

val generateFdbDef = tasks.register("generateFdbDef") {
    description = "Create fdb.def with absolute path for options that gradle DSL cannot handle" +
        " or cinterop cli doesn't support on Linux"

    doLast {
        val fdbNativeDir = project.projectDir.resolve("../packages/firebase_ffi/native")
        val outputFile = project.file("src/nativeInterop/cinterop/fdb.def")
        outputFile.parentFile.mkdirs()
        val soDir = fdbNativeDir.resolve("build").canonicalPath
        outputFile.writeText(
            """
            |headers = firebase_bridge.h
            |headerFilter = firebase_bridge.h kn_bridge.h
            |linkerOpts = -L$soDir -lfirebase_ffi -rpath $soDir
            """.trimMargin()
        )
    }
}
tasks.named("cinteropFdbLinuxX64") { dependsOn(generateFdbDef) }
