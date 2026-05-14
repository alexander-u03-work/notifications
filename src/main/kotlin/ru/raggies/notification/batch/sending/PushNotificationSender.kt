package ru.raggies.notification.batch.sending

import org.springframework.stereotype.Component
import ru.raggies.notification.batch.NotificationBatchData
import ru.raggies.notification.model.NotificationDeliveryType
import ru.raggies.notification.notifications.users.UserNotificationManager

@Component
class PushNotificationSender(
    private val userNotificationManager: UserNotificationManager
): NotificationSender {

    override fun getType(): NotificationDeliveryType = NotificationDeliveryType.Push

    override fun sendNotification(data: NotificationBatchData, message: String) {
        userNotificationManager.createUserNotification(data.userSubject, data.notificationName, message)
    }

}
