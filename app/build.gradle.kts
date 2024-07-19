group = "pl.dpotyralski"
version = "1.0"

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "pl.dpotyralski.AppKt"
    }
}

dependencies {
    implementation("io.javalin:javalin:6.1.6")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("commons-io:commons-io:2.16.1")
    implementation("dev.misfitlabs.kotlinguice4:kotlin-guice:3.0.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.12")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

// tasks.named("build")  {
//    dependsOn("shadowJar")
// }
