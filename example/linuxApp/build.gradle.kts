plugins {
    alias(libs.plugins.kotlin)
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
