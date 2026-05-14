package ru.raggies.notification.event.service

import ru.raggies.notification.event.NotificationEvent

class SuggestionImprovementEvent(
    source: SuggestionImprovementRequest,
    operation: String,
    val adminSubjects: List<String>,
) : NotificationEvent<SuggestionImprovementRequest>(source, operation)