package ru.raggies.notification.notifications.users

import ru.raggies.notification.notifications.Notification

data class UserNotificationSubscription(
    val userId: Long,
    val email: String,
    val notification: Notification,
    val active: Boolean
) {

    fun notificationName() = notification.name

}
