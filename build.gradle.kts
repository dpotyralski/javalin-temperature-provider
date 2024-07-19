plugins {
    kotlin("jvm") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }
}

kotlin {
    jvmToolchain(17)
}
