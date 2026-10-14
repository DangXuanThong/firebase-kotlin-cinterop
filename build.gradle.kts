plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.mavenPublish) apply false
}

subprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        extensions.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral()
            signAllPublications()
            pom {
                developers {
                    developer {
                        id = "dangxuanthong"
                        name = "Dang Xuan Thong"
                        email = "dangxuanthongvn@gmail.com"
                        organization = "Dang Xuan Thong"
                        organizationUrl = "https://github.com/dangxuanthong"
                    }
                }
            }
        }
    }
}
