package ru.raggies.notification.templates

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TemplatesController.BASE_URL)
class TemplatesController(
    private val templateManager: TemplateManager
) {

    companion object {
        const val BASE_URL = "/api/templates"
    }

    //todo: creation and update templates

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    fun getTemplates(): List<Template> = templateManager.getTemplates()

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/name")
    fun getTemplates(@RequestParam name: String) =
        templateManager.getTemplateByName(name)

}