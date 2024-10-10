repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":commons"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}