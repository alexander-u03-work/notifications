package ru.raggies.notification.tests

import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import ru.raggies.notification.subscribtions.SubscriptionController


@Component
class AppHelper(
    private val testWebClient: TestWebClient
) {

    fun tryToGetUsersSubscriptions(userId: Long): List<UserNotificationSubscription> = testWebClient
        .requestList(UserNotificationSubscription::class.java) { client ->
            client.perform(get(SubscriptionController.BASE_URL + "/{userId}/notifications/subscriptions", userId))
        }

}