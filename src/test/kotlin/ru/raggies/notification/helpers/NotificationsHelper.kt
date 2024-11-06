package ru.raggies.notification.helpers

import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import ru.raggies.notification.notifications.Notification
import ru.raggies.notification.notifications.NotificationType
import ru.raggies.notification.notifications.NotificationsController
import ru.raggies.notification.utils.TestWebClient

@Component
class NotificationsHelper(
    private val testWebClient: TestWebClient
) {

    fun tryToGetNotifications(jwt: Jwt): List<Notification> = testWebClient
        .requestList(Notification::class.java){ client ->
            client.perform(get(NotificationsController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()
                    .jwt(jwt)
                    .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }

    fun tryToGetNotificationTypes(jwt: Jwt): List<NotificationType> = testWebClient
        .requestList(NotificationType::class.java){ client ->
            client.perform(get(NotificationsController.BASE_URL + "/types")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt()
                        .jwt(jwt)
                        .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }
}