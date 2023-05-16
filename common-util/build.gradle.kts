import org.jetbrains.intellij.tasks.PrepareSandboxTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

evaluationDependsOn(":api")

description = "Kotlin Symbol Processing Util"

val kotlinBaseVersion: String by project
val intellijVersion: String by project

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs.add("-Xjvm-default=all-compatibility")
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
    id("org.jetbrains.dokka")
}

intellij {
    version = intellijVersion
}

dependencies {
    implementation(kotlin("stdlib", kotlinBaseVersion))
    implementation("org.jetbrains.kotlin:kotlin-compiler:$kotlinBaseVersion")
    implementation(project(":api"))
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.withType<PrepareSandboxTask> {
    val optionsDir = "${this.configDirectory}/options"
    outputs.dir(optionsDir).withPropertyName("Configuration options directory")
    outputs.file("$optionsDir/updates.xml").withPropertyName("Configuration options updates settings")
}

tasks.test {
    dependsOn("prepareTestingSandbox")
}
