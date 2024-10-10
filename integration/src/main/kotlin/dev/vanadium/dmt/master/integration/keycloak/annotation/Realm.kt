package dev.vanadium.dmt.master.integration.keycloak.annotation

import org.springframework.beans.factory.annotation.Qualifier


@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Realm(

)
