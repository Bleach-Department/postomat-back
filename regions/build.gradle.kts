plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin") version "2.1.3"
//    id("com.palantir.docker")
//    id("com.palantir.docker-run")
}



repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

val exposedVersion = "0.40.1"

dependencies {
    implementation(project(":stub"))
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("ch.qos.logback:logback-classic:1.4.4")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.8.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

ktor {
    docker {
        localImageName.set("$group-${project.name}")
        imageTag.set(version.toString())
    }
}