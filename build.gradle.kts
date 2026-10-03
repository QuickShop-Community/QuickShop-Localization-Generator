plugins {
    application
}

group = "org.quickshop.tools"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.3")
    implementation("com.google.code.gson:gson:2.13.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {

    mainClass = "org.quickshop.tools.localization.LocalizationDocGenerator"
}

tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Builds a standalone executable JAR containing all runtime dependencies."
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter {
            it.exists()
        }.map {
            if (it.isDirectory) {
                it
            } else {
                zipTree(it)
            }
        }
    })
}

tasks.named("build") {
    dependsOn("fatJar")
}
