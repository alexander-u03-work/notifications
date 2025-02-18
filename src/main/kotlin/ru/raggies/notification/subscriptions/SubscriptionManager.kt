package ru.raggies.notification.subscriptions

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.events.user.User
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.notifications.NotificationManager

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Component
class SubscriptionManager(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: NotificationSubscriptionRepository
) {

    private val userSubscriptions: LoadingCache<Long, List<UserSubscription>> = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build(CacheLoader.from(::createUserSubscriptionsCache))

    @Transactional
    fun subscribeUser(user: User) {
        val notifications = notificationManager.getNotifications()

        val entities = notifications.map { notification ->
            NotificationSubscriptionEntity(user, notification, true)
        }
        subscriptionRepository.saveAll(entities)

        userSubscriptions.invalidate(user.id)
    }

    @Transactional
    fun unsubscribeUser(userId: Long, notificationId: Long): UserSubscription {
        val subscription = subscriptionRepository.findByUserIdAndNotificationId(userId, notificationId) ?:
            throw ApplicationException("Can not unsubscribe user with id $userId for notification with id $notificationId")

        if (subscription.notification!!.isRequired!!) {
            throw ApplicationException("Notification with id $notificationId required to subscribe")
        }
        subscription.active = false
        val savedEntity = subscriptionRepository.save(subscription)

        userSubscriptions.invalidate(savedEntity.userId!!)
        return savedEntity.toDTO()
    }

    fun getUserSubscriptions(notificationName: String, userIds: List<Long>): List<UserSubscription> =
        userIds
            .flatMap { userId -> getUserSubscriptions(userId) }
            .filter { subscription -> subscription.active }
            .filter { subscription -> notificationName == subscription.notification.name }


    fun getUserSubscriptions(userId: Long): List<UserSubscription> =
        try {
            userSubscriptions.get(userId)
        } catch (e: ExecutionException) {
            emptyList()
        }

    private fun createUserSubscriptionsCache(userId: Long) =
        subscriptionRepository.findByUserId(userId)
            .map { it.toDTO() }

}