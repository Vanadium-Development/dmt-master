plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "dmt-master"

include("application", "domainmodel")
include("persistence")
include("service")
include("authentication")
