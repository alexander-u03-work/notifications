package ru.raggies.notification.notifications

open class NotificationRequest(
    val notificationName: String,
    val messageArgs: Map<String, String>
)