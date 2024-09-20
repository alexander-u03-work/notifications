package ru.raggies.notification.notifications.users

import org.springframework.stereotype.Component

@Component
class UserNotificationManager(
    private val subscriptionRepository: NotificationSubscriptionRepository,
    private val userNotificationRepository: UserNotificationRepository
) {

    fun getSubscribedNotifications(userId: Long, notificationName: String): List<UserNotificationSubscription> =
        subscriptionRepository.findAllByUserIdAndNotificationNameAndActive(userId, notificationName, true)
            .map { it.toDTO() }

    fun getSubscribedNotifications(notificationName: String): List<UserNotificationSubscription> =
        subscriptionRepository.findByNotificationNameAndActive(notificationName, true)
            .map { it.toDTO() }


    fun getUserNotifications(userId: Long): List<UserNotification> =
        userNotificationRepository.findAllByOrderBySentAtDesc()
            .map { it.toDTO() }

}