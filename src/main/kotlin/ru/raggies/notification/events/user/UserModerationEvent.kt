package ru.raggies.notification.events.user

import ru.raggies.notification.events.NotificationEvent

class UserModerationEvent(
    source: User,
    operation: String,
    val issueType: String?,
    val comment: String?,
): NotificationEvent<User>(source, operation)