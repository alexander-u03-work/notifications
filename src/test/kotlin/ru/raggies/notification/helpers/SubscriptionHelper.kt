package ru.raggies.notification.helpers

import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import ru.raggies.notification.subscribtions.SubscriptionController
import ru.raggies.notification.utils.TestWebClient


@Component
class SubscriptionHelper(
    private val testWebClient: TestWebClient
) {

    fun tryToGetUsersSubscriptions(userId: Long, jwt: Jwt): List<UserNotificationSubscription> = testWebClient
        .requestList(UserNotificationSubscription::class.java) { client ->
            client.perform(get(SubscriptionController.BASE_URL + "/{userId}/notifications/subscriptions", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()
                    .jwt(jwt)
                    .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }
}