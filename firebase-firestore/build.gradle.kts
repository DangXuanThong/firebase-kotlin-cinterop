plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
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
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
            implementation("com.squareup.okio:okio:3.18.2")
        }
        linuxX64Main.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.11.0")
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
