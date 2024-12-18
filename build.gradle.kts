import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25" apply false
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"  apply false
    id("org.springframework.boot") version "3.3.3" apply false
}

group = "dev.vanadium.dmt.master"
version = "1.0-SNAPSHOT-${getGitCommitHash()}"


fun getGitCommitHash(): String {
    val out = ByteArrayOutputStream()

    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = out
    }

    return out.toString("utf-8").trim()
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-jpa")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation(kotlin("stdlib"))
        implementation("org.apache.commons:commons-lang3:3.17.0")
        implementation("org.keycloak:keycloak-admin-client:25.0.6")
        implementation("io.minio:minio:8.5.12")


        implementation("com.auth0:java-jwt:4.4.0")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }

    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}