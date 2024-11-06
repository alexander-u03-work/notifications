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
import ru.raggies.notification.TestNotificationApplication
import ru.raggies.notification.helpers.JwtHelper
import ru.raggies.notification.helpers.TemplateHelper
import java.util.*
import java.util.concurrent.TimeUnit

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemplatesTests {

    @Autowired
    lateinit var templateHelper: TemplateHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper

    private lateinit var defaultJwt: Jwt

    @BeforeEach
    fun init() {
        val subject = UUID.randomUUID().toString()
        defaultJwt = jwtHelper.generateJwt(subject, 90)
    }

    @Test
    fun `Should return template list` () {
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted {
            val templates = templateHelper.tryToGetTemplates(defaultJwt)
            assertTrue(templates.isNotEmpty())

            templates.forEach {
                assertTrue(it.content.isNotEmpty())
            }
        }
    }
    @Test
    fun `Should return template list by name` () {
        Awaitility.await().atMost(20, TimeUnit.SECONDS).untilAsserted {
            val template = templateHelper.tryToGetTemplate("SystemNotification", defaultJwt)

            assertTrue(template.content.isNotEmpty())
        }
    }


}