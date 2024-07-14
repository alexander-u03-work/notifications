package ru.raggies.notification.subscribtions

import org.springframework.web.bind.annotation.*
import ru.raggies.notification.notifications.UserNotificationManager
import ru.raggies.notification.tables.pojos.UserNotificationSubscription

@RestController
@RequestMapping("/users")
class UserNotificationController(
    private val userNotificationManager: UserNotificationManager
) {

    @PostMapping("/{userId}/notifications")
    fun subscribeUserForNotificationType(
        @PathVariable userId: Long,
        @RequestBody request: SubscribedNotificationRequest
    ): UserNotificationSubscription =
        userNotificationManager.subscribeUserForNotificationType(userId, request)

    @GetMapping("/{userId}/notifications/types")
    fun getNotificationTypes(@PathVariable userId: Long): List<SubscriptionNotificationInfo> =
        userNotificationManager.getUserNotificationTypesForUser(userId)

    @GetMapping("/{userId}/notifications")
    fun getUsersNotifications(@PathVariable userId: Long): List<UserNotificationInfo> =
        userNotificationManager.getUserNotifications(userId)

}