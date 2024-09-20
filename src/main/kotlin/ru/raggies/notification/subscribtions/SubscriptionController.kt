package ru.raggies.notification.subscribtions

import org.springframework.web.bind.annotation.*
import ru.raggies.notification.notifications.users.UserNotificationSubscription

@RestController
@RequestMapping(SubscriptionController.BASE_URL)
class SubscriptionController(
    private val subscriptionManager: SubscriptionManager,
) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @DeleteMapping("/{userId}/notifications/{notificationId}/subscriptions")
    fun unsubscribeUserForNotificationType(
        @PathVariable userId: Long,
        @PathVariable notificationId: Long
    ): UserNotificationSubscription =
        subscriptionManager.unsubscribeUser(userId, notificationId)


    @GetMapping("/{userId}/notifications/subscriptions")
    fun getUserSubscriptions(@PathVariable userId: Long): List<UserNotificationSubscription> =
        subscriptionManager.getUserSubscriptions(userId)

}