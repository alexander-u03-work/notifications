package ru.raggies.notification.templates

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.tables.pojos.Template

@RestController
@RequestMapping("/api/templates")
class TemplatesController(
    private val templateManager: TemplateManager
) {

    //todo: creation and update templates

    @GetMapping
    fun getTemplates(): List<Template> = templateManager.getTemplates()

    @GetMapping("/name")
    fun getTemplates(@RequestParam name: String): Template {
        return templateManager.getTemplateByName(name) ?:
            throw ApplicationException("Template wit $name not found")
    }

}