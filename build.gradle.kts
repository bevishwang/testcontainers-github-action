plugins {
    java
    id("java-library")
//    kotlin("jvm") version "1.7.21"
}

group = "com.citadel.csds"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("dev.ocpd.slf4k:slf4k:0.2.0")

    implementation("org.springframework.boot:spring-boot:2.7.11")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.11")
    implementation("org.springframework.boot:spring-boot-starter:2.7.11")

    // Google Cloud Storage
    implementation(platform("com.google.cloud:libraries-bom:26.12.0"))
    implementation("com.google.cloud:google-cloud-storage")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.11")
    testImplementation("org.testcontainers:testcontainers:1.16.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}
