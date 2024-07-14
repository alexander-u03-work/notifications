package ru.raggies.notification.subscribtions

import ru.raggies.notification.tables.pojos.UserNotification
import java.time.Instant

data class UserNotificationInfo(
    val id: Long,
    val userId: Long,
    val notificationName: String,
    val message: String,
    val sentAt: Instant
) {
    constructor(userNotification: UserNotification) :
        this(
            userNotification.id!!,
            userNotification.userId!!,
            userNotification.notificationName!!,
            userNotification.message.toString(),
            userNotification.sentAt!!
        )
}
