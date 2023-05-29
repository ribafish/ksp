rootProject.name = "ksp"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap/")
        maven("https://www.jetbrains.com/intellij-repository/snapshots")
    }
}

plugins {
    id("com.gradle.enterprise") version("3.13.1")
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.10"
}

gradleEnterprise {
    server = "https://ec2-3-237-201-221.compute-1.amazonaws.com"
    allowUntrustedServer = true

    buildScan {
        publishAlways()
        isUploadInBackground = System.getenv("CI") == null
        capture {
            // for plugin >= 3.7
            isTaskInputFiles = true
        }
    }
}

include("api")
include("gradle-plugin")
include("common-util")
include("test-utils")
include("compiler-plugin")
include("symbol-processing")
include("symbol-processing-cmdline")
include("integration-tests")
include("kotlin-analysis-api")
