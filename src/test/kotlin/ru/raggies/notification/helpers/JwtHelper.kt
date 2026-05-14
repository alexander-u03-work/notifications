package ru.raggies.notification.helpers

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import ru.raggies.notification.config.RabbitMqProperties
import ru.raggies.notification.event.user.User
import ru.raggies.notification.event.user.UserRegisterEvent
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Component
class JwtHelper(
    private val rabbitTemplate: RabbitTemplate,
    private val properties: RabbitMqProperties
) {

    fun generateJwt(
        userSubject: String = UUID.randomUUID().toString(),
        seconds: Long = 90
    ): Jwt {
        val now = Instant.now()
        return Jwt.withTokenValue("token")
            .issuedAt(now)
            .expiresAt(now.plus(seconds, ChronoUnit.SECONDS))
            .header("alg", "none")
            .subject(userSubject)
            .build()
    }

    fun registerUser(user: User = getUser()): User {
        val event = UserRegisterEvent(user, "CREATE")
        rabbitTemplate.convertAndSend(properties.queues.usersRegistration.queueName, event)
        return user
    }

    fun getUser(
        userSubject: String = UUID.randomUUID().toString(),
        email: String = "${userSubject}@raggies.ru",
    ) = User(userSubject, email)

}