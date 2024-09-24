
dependencies {

    implementation(project(":domainmodel"))
    implementation(project(":persistence"))


    implementation("org.springframework.boot:spring-boot-starter-data-jpa")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}