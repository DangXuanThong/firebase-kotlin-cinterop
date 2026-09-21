plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    linuxX64 {
        binaries {
            executable { entryPoint = "main" }
        }
    }

    sourceSets {
        linuxX64Main.dependencies {
            implementation(projects.firebaseFirestore)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
        }
    }
}
