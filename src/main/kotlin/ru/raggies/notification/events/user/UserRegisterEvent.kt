package ru.raggies.notification.events.user

import ru.raggies.notification.events.NotificationEvent

class UserRegisterEvent(
    source: User,
    operation: String,
): NotificationEvent<User>(source, operation)