package ru.raggies.notification.it

import org.awaitility.Awaitility
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.test.context.ActiveProfiles
import ru.raggies.notification.helpers.JwtHelper
import ru.raggies.notification.notifications.NotificationProperties
import ru.raggies.notification.notifications.PersonalNotificationRequest
import ru.raggies.notification.notifications.sending.NotificationDeliveryProvider
import ru.raggies.notification.subscribtions.SubscribeRequest
import ru.raggies.notification.helpers.SubscriptionHelper
import ru.raggies.notification.TestNotificationApplication
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriptionTests {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate
    @Autowired
    lateinit var properties: NotificationProperties
    @Autowired
    lateinit var subscriptionHelper: SubscriptionHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper

    @SpyBean
    lateinit var notificationDeliveryProvider: NotificationDeliveryProvider

    private lateinit var defaultJwt: Jwt

    private val idCounter = 1L

    @BeforeEach
    fun init() {
        val subject = UUID.randomUUID().toString()
        defaultJwt = jwtHelper.generateJwt(subject, 90)
    }

    @AfterEach
    fun inc() {
        idCounter.inc()
    }

    @Test
    fun `Should subscribe user for all notifications`() {
        val userId = 1L
        subscribeUser(userId = idCounter)

        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted {
            val subscriptions = subscriptionHelper.tryToGetUsersSubscriptions(userId, defaultJwt)
                .groupBy { it.notification.name }
            assertTrue(subscriptions.isNotEmpty())

            subscriptions.forEach {
                assertTrue(it.key.isNotEmpty())
                assertTrue(it.value.isNotEmpty())
            }
        }
    }

    @Disabled
    @Test
    fun `Should send personal notification and send message`() {
        val userId = idCounter
        subscribeUser(userId)
        createPersonalNotification(userId)

        Awaitility.await().atMost(90, TimeUnit.SECONDS).untilAsserted {
            verify(notificationDeliveryProvider, times(1))
                .write(any())
        }
    }

    private fun subscribeUser(
        userId: Long,
        email: String = "${UUID.randomUUID()}@raggies.ru",
    ) {
        val request = SubscribeRequest(userId, email)
        rabbitTemplate.convertAndSend(properties.subscribeOnRegisterQueueName(), request)
    }

    private fun createPersonalNotification(
        userId: Long,
        notificationName: String = "OrderNotification",
        args: Map<String, String> = mapOf(
            "orderId" to UUID.randomUUID().toString(),
            "expectedDeliveryTime" to Instant.now().toString(),
            "payment" to "payment"
        )
    ) {
        val request = PersonalNotificationRequest(userId, notificationName, args)
        rabbitTemplate.convertAndSend(properties.personalQueueName(), request)
    }

}