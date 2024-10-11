package dev.vanadium.dmt.master.commons.error

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.correlation.MDCUtils
import dev.vanadium.dmt.master.domainmodel.incident.Incident
import dev.vanadium.dmt.master.persistence.IncidentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class IncidentService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var incidentRepository: IncidentRepository

    @Autowired
    private lateinit var userContext: UserContext

    fun storeIncident(exception: Exception): Incident {
        val correlationId = MDCUtils.getCorrelationId()

        val incident =
            incidentRepository.save(Incident(correlationId, exception.stackTraceToString(), Instant.now(), null, null))

        logger.info("Stored incident $incident")

        return incident
    }

    fun getIncidents(resolved: Boolean, pageable: Pageable): Page<Incident> {
        return incidentRepository.findByResolved(resolved, pageable)
    }

    fun resolveIncident(incidentId: UUID) {
        val incident = getById(incidentId) ?: throw DmtError.INCIDENT_NOT_FOUND.toThrowableException(null, incidentId)

        incident.resolve(userContext.tenant)

        store(incident)

        logger.info("Incident $incident was successfully resolved by $userContext")
    }

    fun store(incident: Incident): Incident {

        if(incident.isResolved() && incident.resolvedBy == null) {
            throw DmtError.INTERNAL_VALIDATION_ERROR.toThrowableException(null, "The incident $incident cannot be stored as resolved while the resolvedBy field is null.")
        }

        if(!incident.isResolved() && incident.resolvedBy != null) {
            throw DmtError.INTERNAL_VALIDATION_ERROR.toThrowableException(null, "The incident $incident cannot be stored as not-resolved when the resolvedBy field is not null. ")
        }

        return this.incidentRepository.save(incident)
    }

    fun getById(incidentId: UUID): Incident? {
        return incidentRepository.findById(incidentId).getOrNull()
    }
}
