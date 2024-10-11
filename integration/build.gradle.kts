repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":commons"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}