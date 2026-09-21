@file:Suppress("UnstableApiUsage")

rootProject.name = "firebase-kotlin-cinterop"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":firebase-firestore")
include(":example")
