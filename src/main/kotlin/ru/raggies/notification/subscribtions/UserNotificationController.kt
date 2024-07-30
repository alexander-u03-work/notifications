package ru.raggies.notification.subscribtions

import org.springframework.web.bind.annotation.*
import ru.raggies.notification.notifications.UserNotificationManager
import ru.raggies.notification.tables.pojos.UserNotificationSubscription

@RestController
@RequestMapping("/users")
class UserNotificationController(
    private val subscriptionManager: UserSubscriptionManager,
    private val userNotificationManager: UserNotificationManager
) {

    @PostMapping("/{userId}/subscriptions")
    fun unsubscribeUserForNotificationType(
        @PathVariable userId: Long,
        @RequestBody request: UnsubscribeUserRequest
    ): UserNotificationSubscription =
        subscriptionManager.unsubscribeUserFromNotifications(userId, request)

    @GetMapping("/{userId}/notifications/types")
    fun getNotificationTypes(@PathVariable userId: Long): List<UserNotificationSubscriptionType> =
        userNotificationManager.getUserNotificationTypesForUser(userId)

    @GetMapping("/{userId}/notifications")
    fun getUsersNotifications(@PathVariable userId: Long): List<NotificationInfo> =
        userNotificationManager.getUserNotifications(userId)

}