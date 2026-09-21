plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    linuxX64 {
        compilations["main"].cinterops {
            create("fdb") {
                defFile(project.file("src/nativeInterop/cinterop/fdb.def"))
            }
        }
        binaries {
            executable { entryPoint = "main" }
        }
    }

    sourceSets {
        linuxX64Main.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.11.0")
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
