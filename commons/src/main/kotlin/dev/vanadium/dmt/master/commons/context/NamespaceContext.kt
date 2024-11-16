package dev.vanadium.dmt.master.commons.context

import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class NamespaceContext : RequestContextHolder<Namespace>() {
    override fun contextHolderName(): String {
        return this::class.qualifiedName!!
    }
}