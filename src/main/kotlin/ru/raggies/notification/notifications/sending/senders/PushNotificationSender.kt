package ru.raggies.notification.notifications.sending.senders

import org.springframework.stereotype.Component
import ru.raggies.notification.model.NotificationDeliveryType
import ru.raggies.notification.model.UserNotificationEntity
import ru.raggies.notification.notifications.users.UserNotificationRepository
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import java.time.Instant

@Component
class PushNotificationSender(
    private val userNotificationRepository: UserNotificationRepository
): NotificationSender {

    override fun canSend(type: NotificationDeliveryType) =
        type == NotificationDeliveryType.Push

    override fun sendNotification(info: UserNotificationSubscription, htmlBody: String) {
        userNotificationRepository.save(UserNotificationEntity(
            userId = info.userId,
            notificationName = info.notificationName(),
            message = htmlBody,
            sentAt = Instant.now(),
        ))
    }

}
