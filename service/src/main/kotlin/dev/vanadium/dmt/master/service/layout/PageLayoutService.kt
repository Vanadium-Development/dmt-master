package dev.vanadium.dmt.master.service.layout

import org.springframework.stereotype.Service

@Service
class PageLayoutService {


    fun mainMenuBar(): MenuBar {
        return menuBar {
            category("Files") {
                page("My files", "/files", "file", listOf())
            }
        }
    }
}