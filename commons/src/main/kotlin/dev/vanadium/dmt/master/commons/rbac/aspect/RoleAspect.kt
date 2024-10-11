package dev.vanadium.dmt.master.commons.rbac.aspect

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.commons.rbac.RequireRole
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class RoleAspect {

    @Autowired
    private lateinit var userContext: UserContext

    @Pointcut("@annotation(dev.vanadium.dmt.master.commons.rbac.RequireRole)")
    fun securedMethod() {}

    @Before("securedMethod()")
    fun handleRequireRole(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature

        val annotation = signature.method.getAnnotation(RequireRole::class.java)

        if(!userContext.roles.any { annotation.roles.map { role -> role.roleName }.contains(it) }) {
            throw DmtError.INSUFFICIENT_PERMISSIONS.toThrowableException(null)
        }
    }
}