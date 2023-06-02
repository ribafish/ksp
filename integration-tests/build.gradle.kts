import com.google.devtools.ksp.RelativizingPathProvider

val junitVersion: String by project
val kotlinBaseVersion: String by project
val agpBaseVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation("junit:junit:$junitVersion")
    testImplementation(gradleTestKit())
    testImplementation("org.jetbrains.kotlin:kotlin-compiler:$kotlinBaseVersion")

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.0")
}

tasks.named<Test>("test") {
    systemProperty("kotlinVersion", kotlinBaseVersion)
    systemProperty("kspVersion", version)
    systemProperty("agpVersion", agpBaseVersion)
    jvmArgumentProviders.add(RelativizingPathProvider("testRepo", File(rootProject.buildDir, "repos/test")))
    dependsOn(":api:publishAllPublicationsToTestRepository")
    dependsOn(":gradle-plugin:publishAllPublicationsToTestRepository")
    dependsOn(":symbol-processing:publishAllPublicationsToTestRepository")
    dependsOn(":symbol-processing-cmdline:publishAllPublicationsToTestRepository")

    // JDK_9 environment property is required.
    // To add a custom location (if not detected automatically) follow https://docs.gradle.org/current/userguide/toolchains.html#sec:custom_loc
    if (System.getenv("JDK_9") == null) {
        val launcher9 = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(9))
        }
        environment["JDK_9"] = launcher9.map { it.metadata.installationPath }
    }

    // Java 11 is required to run tests
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })

    useJUnitPlatform()
    distribution {
        enabled.set(false)
        maxLocalExecutors.set(0)
        maxRemoteExecutors.set(1)
        requirements.set(setOf("my-local-agent", "jdk=11", "jdk=9"))
    }

    // JDK_9 environment property is required.
    // To add a custom location (if not detected automatically) follow https://docs.gradle.org/current/userguide/toolchains.html#sec:custom_loc
    if (System.getenv("JDK_9") == null) {
        val launcher9 = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(9))
        }
        environment["JDK_9"] = launcher9.map { it.metadata.installationPath }
    }
}
