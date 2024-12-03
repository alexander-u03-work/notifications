package ru.raggies.notification.subscribtions

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.notifications.NotificationManager
import ru.raggies.notification.notifications.users.NotificationSubscriptionRepository
import ru.raggies.notification.notifications.users.UserNotificationSubscription

@Component
class SubscriptionManager(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: NotificationSubscriptionRepository
) {

    @Transactional
    fun subscribeUser(request: SubscribeRequest): List<UserNotificationSubscription> {
        val notifications = notificationManager.getNotifications()

        val toSubscribe = notifications.map { notification ->
            NotificationSubscriptionEntity(request.userId, notification, request.email, true)
        }
        return subscriptionRepository.saveAll(toSubscribe)
            .map { it.toDTO() }
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

    fun getSubscribedNotifications(userId: Long, notificationName: String): List<UserNotificationSubscription> =
        subscriptionRepository.findAllByUserIdAndNotificationNameAndActive(userId, notificationName, true)
            .map { it.toDTO() }

    fun getSubscribedNotifications(notificationName: String): List<UserNotificationSubscription> =
        subscriptionRepository.findByNotificationNameAndActive(notificationName, true)
            .map { it.toDTO() }

    fun getUserSubscriptions(userId: Long): List<UserNotificationSubscription> =
        subscriptionRepository.findByUserId(userId)
            .map { it.toDTO() }

}