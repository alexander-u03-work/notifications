package ru.raggies.notification.events.service

data class SuggestionImprovementRequest(
    val username: String,
    val suggestions: String,
)