package ru.raggies.notification.helpers

import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import ru.raggies.notification.subscriptions.UserSubscription
import ru.raggies.notification.subscriptions.SubscriptionController
import ru.raggies.notification.utils.TestWebClient


@Component
class SubscriptionHelper(
    private val testWebClient: TestWebClient
) {

    fun tryToGetUserSubscriptions(jwt: Jwt): List<UserSubscription> = testWebClient
        .requestList(UserSubscription::class.java) { client ->
            client.perform(get(SubscriptionController.BASE_URL + "/notifications/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()
                    .jwt(jwt)
                    .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }
}