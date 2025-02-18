package ru.raggies.notification.it

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import ru.raggies.notification.TestNotificationApplication
import ru.raggies.notification.helpers.JwtHelper
import ru.raggies.notification.helpers.NotificationsHelper

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationsTests {

    @Autowired
    lateinit var notificationsHelper: NotificationsHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper

    private val defaultJwt by lazy {
        jwtHelper.generateJwt()
    }

    @Test
    fun `Should return notification list`() {
        val notifications = notificationsHelper.tryToGetNotifications(defaultJwt)
            .groupBy { it.name }
        assertTrue(notifications.isNotEmpty())

        notifications.forEach {
            assertTrue(it.key.isNotEmpty())
            assertTrue(it.value.isNotEmpty())
        }
    }

    @Test
    fun `Should return notification types list`() {
        val notifications = notificationsHelper.tryToGetNotificationTypes(defaultJwt)
        assertTrue(notifications.isNotEmpty())

        notifications.forEach {
            assertTrue(it.name.name.isNotEmpty())
        }
    }

}