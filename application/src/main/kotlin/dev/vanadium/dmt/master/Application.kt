package dev.vanadium.dmt.master

import dev.vanadium.dmt.master.authentication.properties.AuthenticationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [
	AuthenticationProperties::class
])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
