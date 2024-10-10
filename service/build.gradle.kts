
dependencies {

    implementation(project(":domainmodel"))
    implementation(project(":persistence"))
    implementation(project(":integration"))
    implementation(project(":commons"))


    api("org.springframework.boot:spring-boot-starter-data-jpa")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}