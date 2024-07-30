package ru.raggies.notification.subscribtions

import ru.raggies.notification.enums.NotificationDeliveryType

data class UserNotificationSubscriptionType(
    val userId: Long,
    val notificationName: String,
    val types: List<NotificationDeliveryType>
)
