package ru.raggies.notification.notifications

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.raggies.notification.notifications.batch.NotificationRequestProvider

@Component
class NotificationListener(
    private val notificationHandler: NotificationRequestProvider
) {

    @RabbitListener(queues = ["distribution-notifications"])
    fun notificationsListener(request: NotificationRequest) =
        notificationHandler.handleDistrictNotification(request)

    @RabbitListener(queues = ["personal-notifications"])
    fun personalNotificationsListener(request: PersonalNotificationRequest) =
        notificationHandler.handlePersonalNotification(request)

}
