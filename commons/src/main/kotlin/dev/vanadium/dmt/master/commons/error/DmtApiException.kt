package dev.vanadium.dmt.master.commons.error


class DmtApiException(message: String, val error: DmtError, cause: Throwable? = null) : RuntimeException(message, cause) {

}