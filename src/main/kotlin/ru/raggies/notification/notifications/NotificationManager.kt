package ru.raggies.notification.notifications

import org.springframework.stereotype.Service
import ru.raggies.notification.notifications.users.UserNotification
import ru.raggies.notification.notifications.users.UserNotificationRepository

@Service
class NotificationManager(
    private val userNotificationRepository: UserNotificationRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationTypeRepository: NotificationTypeRepository
) {

    fun getNotifications(): List<Notification> =
        notificationRepository.findAll()
            .map { it.toDTO() }

    fun getNotificationTypes(): List<NotificationType> =
        notificationTypeRepository.findAll()
            .map { it.toDTO() }

    fun getUserNotifications(userId: Long): List<UserNotification> =
        userNotificationRepository.findByUserIdOrderBySentAtDesc(userId)
            .map { it.toDTO() }

}