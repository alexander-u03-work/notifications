package ru.raggies.notification.helpers

import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import ru.raggies.notification.templates.Template
import ru.raggies.notification.templates.TemplatesController
import ru.raggies.notification.utils.TestWebClient

@Component
class TemplateHelper(
    private val testWebClient: TestWebClient
) {

    fun tryToGetTemplates(jwt: Jwt): List<Template> = testWebClient
        .requestList(Template::class.java){ client ->
            client.perform(get(TemplatesController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt()
                        .jwt(jwt)
                        .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }
    fun tryToGetTemplate(name: String, jwt: Jwt): Template = testWebClient
        .requestOne(Template::class.java){ client ->
            client.perform(get(TemplatesController.BASE_URL + "/name")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)
                .with(jwt()
                    .jwt(jwt)
                    .authorities(SimpleGrantedAuthority("ADMIN")))
            )
        }
}