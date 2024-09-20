package ru.raggies.notification.notifications

class PersonalNotificationRequest(
    val userId: Long,
    notificationName: String,
    messageArgs: Map<String, String>
): NotificationRequest(notificationName, messageArgs)
