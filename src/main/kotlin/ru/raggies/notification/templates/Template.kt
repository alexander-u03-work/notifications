package ru.raggies.notification.templates

import ru.raggies.notification.model.TemplateStatus

data class Template(
    val id: Long,
    val title: String,
    val content: String,
    val status: TemplateStatus,
)
