plugins {
    application
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "com.just-ai.jaicf"
version = "1.0.0"

val jaicf = "1.1.3"//"0.7.0"
val slf4j = "1.7.30"
val ktor = "1.5.1"

// Main class to run application on heroku. Either PollingConnectionKt, or WebhookConnectionKt
application {
    mainClassName = "com.just-ai.jaicf.template.connections.AimyboxAndWebhookConnectionKt"

}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.slf4j:slf4j-simple:$slf4j")
    implementation("org.slf4j:slf4j-log4j12:$slf4j")

    implementation("com.just-ai.jaicf:core:$jaicf")
    implementation("com.just-ai.jaicf:mongo:$jaicf")
    implementation("com.just-ai.jaicf:jaicp:$jaicf")
    implementation("com.just-ai.jaicf:caila:$jaicf")

    implementation("com.just-ai.jaicf:telegram:$jaicf")

    implementation("io.ktor:ktor-server-netty:$ktor")

    implementation("com.just-ai.jaicf:aimybox:$jaicf")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

tasks.create("stage") {
    dependsOn("shadowJar")
}
