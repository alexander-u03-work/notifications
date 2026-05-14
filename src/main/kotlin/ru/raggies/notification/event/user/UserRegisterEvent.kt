package ru.raggies.notification.event.user

import ru.raggies.notification.event.NotificationEvent

class UserRegisterEvent(
    source: User,
    operation: String,
): NotificationEvent<User>(source, operation)