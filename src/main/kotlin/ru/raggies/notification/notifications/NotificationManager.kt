package ru.raggies.notification.notifications

import org.springframework.stereotype.Service

@Service
class NotificationManager(
    private val notificationRepository: NotificationRepository,
    private val notificationTypeRepository: NotificationTypeRepository
) {

    fun getNotifications(): List<Notification> =
        notificationRepository.findAll()
            .map { it.toDTO() }

    fun getNotificationTypes(): List<NotificationType> =
        notificationTypeRepository.findAll()
            .map { it.toDTO() }

}