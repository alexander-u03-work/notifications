package ru.raggies.notification.notifications.sending.senders

import ru.raggies.notification.model.NotificationDeliveryType
import ru.raggies.notification.notifications.users.UserNotificationSubscription


interface NotificationSender {

    fun canSend(type: NotificationDeliveryType): Boolean

    fun sendNotification(info: UserNotificationSubscription, htmlBody: String)

}