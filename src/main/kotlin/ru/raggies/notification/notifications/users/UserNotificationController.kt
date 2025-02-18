package ru.raggies.notification.notifications.users

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.raggies.notification.getUserId

@RestController
@RequestMapping(UserNotificationController.BASE_URL)
class UserNotificationController(
    private val userNotificationManager: UserNotificationManager,
) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @GetMapping
    fun getUsersNotifications(@AuthenticationPrincipal jwt: Jwt): List<UserNotification> =
        userNotificationManager.getUserNotifications(jwt.getUserId())
}