package ru.raggies.notification.events.service

import ru.raggies.notification.events.NotificationEvent

class SuggestionImprovementEvent(
    source: SuggestionImprovementRequest,
    operation: String,
    val adminIds: List<Long>,
) : NotificationEvent<SuggestionImprovementRequest>(source, operation)