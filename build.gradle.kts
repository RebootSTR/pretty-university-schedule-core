
plugins {
    application
    kotlin("jvm") version "1.4.32"
    maven
}

group = "rafikov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven ("https://jitpack.io")
}

application {
    mainClass.set("kotlin/Main.kt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}