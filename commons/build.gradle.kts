
dependencies {

    implementation(project(":domainmodel"))
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.swagger.core.v3:swagger-annotations:2.2.25")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}