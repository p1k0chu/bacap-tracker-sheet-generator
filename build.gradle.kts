plugins {
    kotlin("jvm") version("2.2.21")

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

