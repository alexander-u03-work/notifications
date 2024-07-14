package ru.raggies.notification.subscribtions

import ru.raggies.notification.enums.NotificationDeliveryType

data class SubscriptionNotificationInfo(
    val userId: Long,
    val types: List<NotificationDeliveryType>
)
