package ru.raggies.notification.notifications

class PersonalNotificationRequest(
    notificationName: String,
    args: Map<String, String>,
    val email: String,
): NotificationRequest(notificationName, args)
