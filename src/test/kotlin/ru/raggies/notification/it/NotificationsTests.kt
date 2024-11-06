package ru.raggies.notification.it

import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.test.context.ActiveProfiles
import ru.raggies.notification.helpers.JwtHelper
import ru.raggies.notification.helpers.NotificationsHelper
import ru.raggies.notification.TestNotificationApplication
import java.util.*
import java.util.concurrent.TimeUnit

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationsTests {

    @Autowired
    lateinit var notificationsHelper: NotificationsHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper

    private lateinit var defaultJwt: Jwt

    @BeforeEach
    fun init() {
        val subject = UUID.randomUUID().toString()
        defaultJwt = jwtHelper.generateJwt(subject, 90)
    }

    @Test
    fun `Should return notification list` () {
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted {
            val notifications = notificationsHelper.tryToGetNotifications(defaultJwt)
                .groupBy { it.name }
            assertTrue(notifications.isNotEmpty())

            notifications.forEach {
                assertTrue(it.key.isNotEmpty())
                assertTrue(it.value.isNotEmpty())
            }
        }
    }

    @Test
    fun `Should return notification types list` () {
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted {
            val notifications = notificationsHelper.tryToGetNotificationTypes(defaultJwt)
            assertTrue(notifications.isNotEmpty())

            notifications.forEach {
                assertTrue(it.name.name.isNotEmpty())
            }
        }
    }

}