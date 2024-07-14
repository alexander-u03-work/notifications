package ru.raggies.notification.notifications

open class NotificationRequest(
    val notificationName: String,
    val args: Map<String, String>
)