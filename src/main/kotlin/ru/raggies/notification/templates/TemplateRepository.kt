package ru.raggies.notification.templates

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.TemplateEntity

interface TemplateRepository: JpaRepository<TemplateEntity, Long> {

    fun findByName(name: String): TemplateEntity?

}