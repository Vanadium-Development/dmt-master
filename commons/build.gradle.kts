
dependencies {

    implementation(project(":domainmodel"))
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}