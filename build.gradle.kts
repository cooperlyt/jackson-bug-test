plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.cooperlyt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {



    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")

//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")
//
//    implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
//    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.0")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}