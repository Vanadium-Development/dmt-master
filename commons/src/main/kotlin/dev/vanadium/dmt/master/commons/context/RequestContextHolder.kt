package dev.vanadium.dmt.master.commons.context

import dev.vanadium.dmt.master.commons.error.DmtError

abstract class RequestContextHolder<T>(
    var value: T? = null
) {

    abstract fun contextHolderName(): String

    private fun retrieve(): T {
        return value ?: throw DmtError.REQUEST_CONTEXT_HOLDER_INVALID_STATE.toThrowableException(null, contextHolderName())
    }


    operator fun invoke() = retrieve()
}