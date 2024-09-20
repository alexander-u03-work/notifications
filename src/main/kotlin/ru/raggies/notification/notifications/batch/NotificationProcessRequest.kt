package ru.raggies.notification.notifications.batch

import ru.raggies.notification.notifications.users.UserNotificationSubscription

data class NotificationProcessRequest(
    val notification: UserNotificationSubscription,
    val messageArgs: Map<String, String>
)
