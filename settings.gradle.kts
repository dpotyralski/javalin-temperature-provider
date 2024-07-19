plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "javalin-temperature-provider"

include("black-box-tests")
include("app")
