plugins {
    kotlin("jvm") version "2.0.21"
}

group = "io.github.cooperlyt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {


}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")

        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")

        implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
        implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.0")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")



        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}