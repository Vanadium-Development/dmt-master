package dev.vanadium.dmt.master.service.user

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import dev.vanadium.dmt.master.service.file.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class MeService {

    @Autowired
    private lateinit var userContext: UserContext

    @Autowired
    private lateinit var fileService: FileService


    fun getOwnedFiles(pageable: Pageable): Page<DistributedFile> = fileService.getByUser(pageable, userContext.tenant.id)
}