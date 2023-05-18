import org.jetbrains.dokka.gradle.DokkaCollectorTask

forEachDependantProject { projectDep ->
    evaluationDependsOn(projectDep)
}

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

tasks {
    val mergedSourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        forEachDependantProject { projectDep ->
            from(project(projectDep).sourceSets.main.get().allSource)
        }
    }
}

// Can't merge dokka javadocs any other way than with a DokkaCollectorTask
val mergedJavadoc by tasks.register<DokkaCollectorTask>("mergedJavadoc") {
    forEachDependantProject { projectDep ->
        addChildTask("$projectDep:dokkaJavadoc")
    }
}

tasks.register<Jar>("mergedJavadocJar") {
    dependsOn(mergedJavadoc)
    from(mergedJavadoc.outputDirectory)
    archiveClassifier.set("javadoc")
}

inline fun <T> T.forEachDependantProject(action: T.(projectDep: String) -> Any?) {
    listOf(":common-util", ":compiler-plugin").forEach {
        this.action(it)
    }
}
