@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project

plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    id("com.diffplug.spotless") version "6.9.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
    application
}

group = "club.mileshighjazz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.17")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("com.vdurmont:emoji-java:5.1.1")
    implementation("org.litote.kmongo:kmongo:4.6.1")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.357")
    testImplementation(kotlin("test"))
}

spotless {
    kotlin {
        ktlint("0.46.1")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}