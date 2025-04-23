package ru.raggies.notification.events.service

data class SuggestionImprovementRequest(
    val username: String,
    val phoneNumber: String,
    val clientSatisfaction: Long,
    val interfaceComfort: Long,
    val serviceWorkComfort: Long,
    val suggestion: String?,
)