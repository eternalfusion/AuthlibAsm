import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "me.alexe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.ow2.asm/asm
    implementation("org.ow2.asm:asm:9.4")
    // https://mvnrepository.com/artifact/org.ow2.asm/asm-util
    implementation("org.ow2.asm:asm-util:9.4")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}
