plugins {
    kotlin("jvm") version("2.4.10")

    application
}

application {
    mainClass = "io.github.p1k0chu.bacaptrackersheetgenerator.MainKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.github.p1k0chu.bacaptrackersheetgenerator.MainKt"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.poi)
    implementation(libs.clikt)
    implementation(libs.serialization)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

