package ru.raggies.notification.templates

import exception.ApplicationException
import org.springframework.stereotype.Service

@Service
class TemplateManager(
    private val templateRepository: TemplateRepository
) {

    fun getTemplates(): List<Template> = templateRepository.findAll()
        .map { it.toDTO() }

    fun getTemplateByName(templateName: String): Template {
        val template = templateRepository.findByName(templateName) ?:
            throw ApplicationException("Template not found: $templateName")

        return template.toDTO()
    }

}