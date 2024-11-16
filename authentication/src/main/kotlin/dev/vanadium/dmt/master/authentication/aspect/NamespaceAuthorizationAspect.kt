package dev.vanadium.dmt.master.authentication.aspect

import dev.vanadium.dmt.master.commons.context.NamespaceContext
import dev.vanadium.dmt.master.service.namespace.NamespaceService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Aspect
@Component
class NamespaceAuthorizationAspect {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var namespaceService: NamespaceService

    @Autowired
    private lateinit var namespaceContext: NamespaceContext

    @Pointcut("execution(* dev.vanadium.dmt.master.api.controller.AccessUrlController.*(..))")
    fun accessUrlController() {}

    @Pointcut("execution(* dev.vanadium.dmt.master.api.controller.NamespaceController.*(..))")
    fun namespaceController() {}

    @Before("(accessUrlController() || namespaceController()) && args(namespaceId, ..)")
    fun authorizeNamespace(joinPoint: JoinPoint, namespaceId: UUID) {
        logger.debug("Fired namespace aspect for namespace {}", namespaceId)

        val namespace = namespaceService.getAuthorizedNamespace(namespaceId)

        namespaceContext.value = namespace
    }
}