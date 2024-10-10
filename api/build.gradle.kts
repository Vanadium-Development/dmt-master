
dependencies {
    implementation(project(":commons"))
    implementation(project(":domainmodel"))
    implementation(project(":authentication"))
    implementation(project(":integration"))
    implementation(project(":service"))

    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}