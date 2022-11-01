import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.protobuf") version "0.8.18" apply false
    kotlin("jvm") version "1.7.0" apply false
    id("com.palantir.docker") version "0.34.0" apply false
    id("com.palantir.docker-run") version "0.34.0" apply false
}

ext["grpcVersion"] = "1.50.2"
ext["grpcKotlinVersion"] = "1.3.0" // CURRENT_GRPC_KOTLIN_VERSION
ext["protobufVersion"] = "3.21.8"
ext["coroutinesVersion"] = "1.6.4"

allprojects {
    group = "postomat-back"
    version = "0.0.1"
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
//tasks.create("assemble").dependsOn(":server:installDist")