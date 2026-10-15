import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    android {
        namespace = "io.github.dangxuanthong.firebase.core.shared"
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
            create("native") {
                defFile(
                    project.file("build/native.def").apply {
                        if (!exists()) {
                            this.parentFile.mkdirs()
                            createNewFile()
                        }
                        bufferedWriter().use {
                            val soPath = projectDir.resolve("../native/build").canonicalPath
                            it.write(
                                """
                                package = io.github.dangxuanthong.firebase.core.native
                                headers = firebase_compat.h
                                headerFilter = firebase_compat.h
                                linkerOpts = -lfirebase_native -rpath $soPath -L$soPath
                                """.trimIndent()
                            )
                        }
                    }
                )
                includeDirs("$projectDir/../native/include")
            }
        }
        compilerOptions {
            optIn.add(
                "kotlinx.cinterop.ExperimentalForeignApi" // Required for using cinterop features
            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.okio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.gitlive.firebase.app)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

mavenPublishing {
    coordinates(artifactId = "core")
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
