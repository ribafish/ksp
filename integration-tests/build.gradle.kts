import com.google.devtools.ksp.RelativizingLocalPathProvider

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
    testImplementation(project(":api"))
    testImplementation(project(":gradle-plugin"))
    testImplementation(project(":symbol-processing"))
    testImplementation(project(":symbol-processing-cmdline"))
}

val tempTestDir = File(buildDir, "tmp/test")
val cleanupTemporaryTestDir = tasks.register("cleanupTemporaryTestDir", Delete::class.java) {
    delete = setOf(tempTestDir)
}

tasks.test {
    dependsOn(cleanupTemporaryTestDir)
    dependsOn(":api:publishAllPublicationsToTestRepository")
    dependsOn(":gradle-plugin:publishAllPublicationsToTestRepository")
    dependsOn(":symbol-processing:publishAllPublicationsToTestRepository")
    dependsOn(":symbol-processing-cmdline:publishAllPublicationsToTestRepository")

    systemProperty("kotlinVersion", kotlinBaseVersion)
    systemProperty("kspVersion", version)
    systemProperty("agpVersion", agpBaseVersion)

    jvmArgumentProviders.add(RelativizingLocalPathProvider("java.io.tmpdir", tempTestDir))
    val testRepo = File(rootProject.buildDir, "repos/test")
    jvmArgumentProviders.add(RelativizingLocalPathProvider("testRepo", testRepo))

    doFirst {
        if (!testRepo.exists()) testRepo.mkdirs()
    }

    // JDK_9 environment property is required.
    // To add a custom location (if not detected automatically) follow https://docs.gradle.org/current/userguide/toolchains.html#sec:custom_loc
    if (System.getenv("JDK_9") == null) {
        val launcher9 = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(9))
        }
        environment["JDK_9"] = launcher9.map { it.metadata.installationPath }
    }

    maxParallelForks = gradle.startParameter.maxWorkerCount / 2
}
