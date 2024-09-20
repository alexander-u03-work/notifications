package ru.raggies.notification.notifications

import ru.raggies.notification.model.NotificationDeliveryType

data class Notification(
    val id: Long,
    val name: String,
    val isRequired: Boolean,
    val typeId: Long,
    val typeName: NotificationDeliveryType
)
