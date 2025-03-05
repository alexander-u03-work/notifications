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
import ru.raggies.notification.helpers.TemplateHelper

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestNotificationApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemplatesTests {

    @Autowired
    lateinit var templateHelper: TemplateHelper
    @Autowired
    lateinit var jwtHelper: JwtHelper

    private val defaultJwt by lazy {
        jwtHelper.generateJwt()
    }

    //@Test
    fun `Should return template list` () {
        val templates = templateHelper.tryToGetTemplates(defaultJwt)
        assertTrue(templates.isNotEmpty())

        templates.forEach {
            assertTrue(it.content.isNotEmpty())
        }
    }
    //@Test
    fun `Should return template list by name` () {
        val template = templateHelper.tryToGetTemplate("ShopRegisterEventCreateNotification", defaultJwt)

        assertTrue(template.content.isNotEmpty())
    }


}