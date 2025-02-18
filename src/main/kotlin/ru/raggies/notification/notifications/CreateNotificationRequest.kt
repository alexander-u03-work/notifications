package ru.raggies.notification.notifications

data class CreateNotificationRequest(
    val name: String,
    val typeId: Long,
    val isRequired: Boolean,
)