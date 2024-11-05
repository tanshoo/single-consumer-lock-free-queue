plugins {
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:atomicfu:0.26.0")
    testImplementation("org.jetbrains.kotlinx:lincheck:2.34")
    testImplementation("junit:junit:4.13.1")
}

tasks {
    test {
        useJUnit()
    }
}

kotlin {
    jvmToolchain(21)
}