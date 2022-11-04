val ktor_version: String = "2.1.3"

plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":stub"))

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("dev.forst:ktor-openapi-generator:0.5.2")
    implementation("ch.qos.logback:logback-classic:1.4.4")
//    implementation("io.bkbn:kompendium-core:3.5.0")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
}

ktor {
    docker {
        localImageName.set("$group-${project.name}")
        imageTag.set(version.toString())
    }
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}