package ru.raggies.notification.subscribtions

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.model.NotificationSubscriptionPK
import ru.raggies.notification.notifications.NotificationManager
import ru.raggies.notification.notifications.NotificationRepository
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import ru.raggies.notification.notifications.users.NotificationSubscriptionRepository

@Component
class SubscriptionManager(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: NotificationSubscriptionRepository,
    private val notificationRepository: NotificationRepository
) {

    @Transactional
    fun subscribeUser(request: SubscribeRequest): List<UserNotificationSubscription> {
        val notifications = notificationManager.getNotifications().map { it.id }

        return subscriptionRepository.saveAll(
            createSubscriptionEntities(request.userId, request.email, notifications)
        ).map { it.toDTO() }
    }

    @Transactional
    fun unsubscribeUser(userId: Long, notificationId: Long): UserNotificationSubscription {
        val subscription = subscriptionRepository.findByUserIdAndNotificationId(userId, notificationId) ?:
            throw ApplicationException("Can not unsubscribe user with id $userId for notification with id $notificationId")

        if (subscription.notification!!.isRequired!!) {
            throw ApplicationException("Notification with id $notificationId required to subscribe")
        }
        subscription.active = false
        return subscriptionRepository.save(subscription).toDTO()
    }

    @Transactional(readOnly = true)
    fun getUserSubscriptions(userId: Long): List<UserNotificationSubscription> =
        subscriptionRepository.findByUserId(userId)
            .map { it.toDTO() }

    private fun createSubscriptionEntities(userId: Long, email: String, notificationIds: List<Long>): List<NotificationSubscriptionEntity> =
        notificationIds.map { notificationId ->
            notificationRepository.findById(notificationId)
                .map { notification ->
                    NotificationSubscriptionEntity(userId, notificationId, notification, email, false)
                }
                .orElseThrow { throw ApplicationException("Notification with id $notificationId not found") }
        }

}