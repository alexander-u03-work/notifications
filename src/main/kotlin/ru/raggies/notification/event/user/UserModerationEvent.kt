package ru.raggies.notification.event.user

import ru.raggies.notification.event.NotificationEvent

class UserModerationEvent(
    source: User,
    operation: String,
    val issueType: String?,
    val comment: String?,
): NotificationEvent<User>(source, operation)