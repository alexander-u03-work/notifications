package ru.raggies.notification.subscribtions

import ru.raggies.notification.enums.NotificationDeliveryType

data class UnsubscribeUserRequest(
    val notificationName: String,
    val deliveryType: NotificationDeliveryType
)
