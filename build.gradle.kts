import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotest_version: String by project

plugins {
    kotlin("jvm") version "1.4.0"
    application
}
group = "ru.sharashin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}