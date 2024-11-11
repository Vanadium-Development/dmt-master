
dependencies {


    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    api("io.hypersistence:hypersistence-utils-hibernate-63:3.8.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}