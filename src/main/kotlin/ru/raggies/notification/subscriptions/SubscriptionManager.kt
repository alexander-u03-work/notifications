package ru.raggies.notification.subscriptions

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import exception.ApplicationException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.event.user.User
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.notifications.NotificationManager

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Component
class SubscriptionManager(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: NotificationSubscriptionRepository
) {

    private val userSubscriptions: LoadingCache<String, List<UserSubscription>> = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build(CacheLoader.from(::createUserSubscriptionsCache))


    @Transactional
    fun subscribeUser(user: User) {
        val notifications = notificationManager.getNotifications()

        val entities = notifications.map { notification ->
            NotificationSubscriptionEntity(user, notification, true)
        }
        subscriptionRepository.saveAll(entities)

        userSubscriptions.invalidate(user.subject)
    }

    @Transactional
    fun unsubscribeUser(userSubject: String, notificationId: Long): UserSubscription {
        val subscription = subscriptionRepository.findByUserSubjectAndNotificationId(userSubject, notificationId) ?:
            throw ApplicationException("Can not unsubscribe user with subject $userSubject for notification with id $notificationId")

        if (subscription.notification!!.isRequired!!) {
            throw ApplicationException("Notification with id $notificationId required to subscribe")
        }
        subscription.active = false
        val savedEntity = subscriptionRepository.save(subscription)

        userSubscriptions.invalidate(savedEntity.userSubject!!)
        return savedEntity.toDTO()
    }

    fun getUserSubscriptions(notificationName: String, userSubjects: List<String>): List<UserSubscription> =
        userSubjects
            .flatMap { subject -> getUserSubscriptions(subject) }
            .filter { subscription -> subscription.active }
            .filter { subscription -> notificationName == subscription.notification.name }


    fun getUserSubscriptions(userSubject: String): List<UserSubscription> =
        try {
            userSubscriptions.get(userSubject)
        } catch (_: ExecutionException) {
            emptyList()
        }

    private fun createUserSubscriptionsCache(userSubject: String) =
        subscriptionRepository.findByUserSubject(userSubject)
            .map { it.toDTO() }

}