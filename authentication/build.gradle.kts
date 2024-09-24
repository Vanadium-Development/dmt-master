
repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":service"))
    implementation(project(":domainmodel"))
    implementation(project(":persistence"))


    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-oauth2-client")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}