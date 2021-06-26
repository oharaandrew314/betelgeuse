plugins {
    kotlin("jvm") version "1.4.32"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.andrewohara.betelgeuse.Betelgeuse")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("redis.clients:jedis:3.6.1")

}

javafx {
    version = "16"
    modules = listOf("javafx.controls")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}