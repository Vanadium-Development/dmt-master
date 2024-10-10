package dev.vanadium.dmt.master.commons.error

import java.lang.Exception


class DmtApiException(message: String, val error: DmtError, cause: Throwable? = null) : RuntimeException(message) {

}