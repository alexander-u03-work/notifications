package ru.raggies.notification.subscriptions

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import ru.raggies.notification.getUserId

@RestController
@RequestMapping(SubscriptionController.BASE_URL)
class SubscriptionController(
    private val subscriptionManager: SubscriptionManager,
) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @DeleteMapping("/notifications/{notificationId}/subscriptions")
    fun unsubscribeUserForNotificationType(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable notificationId: Long
    ): UserSubscription =
        subscriptionManager.unsubscribeUser(jwt.getUserId(), notificationId)

    @GetMapping("/notifications/subscriptions")
    fun getUserSubscriptions(@AuthenticationPrincipal jwt: Jwt): List<UserSubscription> =
        subscriptionManager.getUserSubscriptions(jwt.getUserId())

}