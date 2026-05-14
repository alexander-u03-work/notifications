package ru.raggies.notification.it

import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import ru.raggies.notification.TestNotificationApplication
import ru.raggies.notification.batch.UserNotificationSender
import ru.raggies.notification.config.RabbitMqProperties
import ru.raggies.notification.event.user.User
import ru.raggies.notification.event.user.UserModerationEvent
import ru.raggies.notification.helpers.JwtHelper
import ru.raggies.notification.helpers.SubscriptionHelper
import java.util.concurrent.TimeUnit

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriptionTests {

    @MockitoSpyBean
    lateinit var rabbitTemplate: RabbitTemplate
    @Autowired
    lateinit var properties: RabbitMqProperties
    @Autowired
    lateinit var subscriptionHelper: SubscriptionHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper
    @MockitoBean
    lateinit var userNotificationSender: UserNotificationSender

    private val defaultUser by lazy {
        jwtHelper.registerUser()
    }

    @Test
    //@Disabled("Fix rabbitmq container")
    fun `Should subscribe user for all notifications`() {
        val jwt = jwtHelper.generateJwt(defaultUser.subject)

        Awaitility.await()
            .atMost(30, TimeUnit.SECONDS)
            .pollDelay(10, TimeUnit.SECONDS)
            .untilAsserted {
                val subscriptions = subscriptionHelper.tryToGetUserSubscriptions(jwt)
                    .groupBy { it.notification.name }
                assertTrue(subscriptions.isNotEmpty())

                subscriptions.forEach {
                    assertTrue(it.key.isNotEmpty())
                    assertTrue(it.value.isNotEmpty())
                }
            }
    }

    @Test
    //@Disabled("Fix rabbitmq container")
    fun `Should publish personal notification and send message`() {
        val jwt = jwtHelper.generateJwt(defaultUser.subject)
        Awaitility.await()
            .atMost(30, TimeUnit.SECONDS)
            .pollDelay(10, TimeUnit.SECONDS)
            .untilAsserted {
                val subscriptions = subscriptionHelper.tryToGetUserSubscriptions(jwt)
                assertTrue(subscriptions.isNotEmpty())
        }

        publishNotification(defaultUser)

        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted {
                verify(userNotificationSender, atLeastOnce())
                    .write(any())
            }
    }

    private fun publishNotification(user: User = jwtHelper.getUser()) {
        val userModerationEvent = UserModerationEvent(user, "CREATE", null, null)
        rabbitTemplate.convertAndSend(properties.queues.usersModeration.queueName, userModerationEvent)
    }


}