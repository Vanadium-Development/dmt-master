
repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":service"))
    implementation(project(":domainmodel"))


    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}