testing {
    suites {
        register<JvmTestSuite>("blackBoxTest") {
            dependencies {
                implementation("org.slf4j:slf4j-simple:2.0.13")
                implementation("org.testcontainers:testcontainers:1.19.8")
                implementation("io.rest-assured:rest-assured:4.4.0")
                implementation("org.hamcrest:hamcrest:2.2")
                implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
            }
        }
    }
}

tasks.named("blackBoxTest") {
    dependsOn(rootProject.findProject("app")?.tasks?.named("shadowJar"))
}
