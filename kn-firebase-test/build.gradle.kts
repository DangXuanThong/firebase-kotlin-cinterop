plugins {
    kotlin("multiplatform") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
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
        nativeMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.11.0")
        }
    }
}
