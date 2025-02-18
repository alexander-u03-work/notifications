package ru.raggies.notification.subscriptions

import ru.raggies.notification.notifications.Notification


data class UserSubscription(
    val userId: Long,
    val email: String,
    val notification: Notification,
    val active: Boolean
)
