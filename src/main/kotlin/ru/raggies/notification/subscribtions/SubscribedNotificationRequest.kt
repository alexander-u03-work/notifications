package ru.raggies.notification.subscribtions

import ru.raggies.notification.enums.NotificationDeliveryType


data class SubscribedNotificationRequest(
    val name: String,
    val deliveryType: NotificationDeliveryType,
    val subscribe: Boolean,
    val email: String
)
