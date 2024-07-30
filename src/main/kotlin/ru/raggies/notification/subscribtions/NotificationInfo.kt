package ru.raggies.notification.subscribtions

import ru.raggies.notification.tables.pojos.UserNotification
import java.time.Instant

data class NotificationInfo(
    val id: Long,
    val userId: Long,
    val notificationName: String,
    val message: String,
    val sentAt: Instant
) {
    constructor(notification: UserNotification) :
        this(
            notification.id!!,
            notification.userId!!,
            notification.notificationName!!,
            notification.message.toString(),
            notification.sentAt!!
        )
}
