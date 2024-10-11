package dev.vanadium.dmt.master.commons.correlation

import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.slf4j.MDC

object MDCUtils {
    private const val CORRELATION_ID_RANDOM_PART_LENGTH = 8

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun populateCorrelationId(correlationId: String?) {
        val value = correlationId ?: generateCorrelationId()
        MDC.put("correlationId", value)
        logger.debug("Populated correlation id '$value'")
    }

    fun getCorrelationId(): String? {
        return MDC.get("correlationId")
    }


    private fun extend(oldCorrelationId: String) = populateCorrelationId("MASTER-${oldCorrelationId.extractRandomPart()}-${generateCorrelationId().extractRandomPart()}")

    operator fun plusAssign(oldCorrelationId: String) = extend(oldCorrelationId)

    private fun generateCorrelationId(): String {
        val randomPart = RandomStringUtils.insecure().nextAlphanumeric(CORRELATION_ID_RANDOM_PART_LENGTH)
        return "MASTER-$randomPart"
    }

    private fun String.extractRandomPart() = this.split("-")[1]
}