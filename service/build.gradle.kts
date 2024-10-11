
dependencies {

    implementation(project(":domainmodel"))
    implementation(project(":persistence"))
    implementation(project(":integration"))
    implementation(project(":commons"))


    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-web")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}