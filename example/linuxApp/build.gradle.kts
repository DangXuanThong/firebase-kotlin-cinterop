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
            implementation(projects.example.shared)
        }
    }
}
