package ru.raggies.notification.notifications.users

import java.time.Instant


data class UserNotification(
    val id: Long,
    val userId: Long,
    val notificationName: String,
    val message: String,
    val sentAt: Instant
)
