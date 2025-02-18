package ru.raggies.notification.helpers

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import ru.raggies.notification.config.RabbitMqProperties
import ru.raggies.notification.events.user.User
import ru.raggies.notification.events.user.UserRegisterEvent
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.atomic.AtomicLong


@Component
class JwtHelper(
    private val rabbitTemplate: RabbitTemplate,
    private val properties: RabbitMqProperties
) {

    var idCounter = AtomicLong(1)

    fun generateJwt(
        userId: Long = idCounter.getAndIncrement(),
        seconds: Long = 90
    ): Jwt {
        val now = Instant.now()
        return Jwt.withTokenValue("token")
            .issuedAt(now)
            .expiresAt(now.plus(seconds, ChronoUnit.SECONDS))
            .header("alg", "none")
            .subject(UUID.randomUUID().toString())
            .claim("user_id", userId)
            .build()
    }

    fun registerUser(user: User = getUser()): User {
        val event = UserRegisterEvent(user, "CREATE")
        rabbitTemplate.convertAndSend(properties.queues.usersRegistration.queueName, event)
        return user
    }

    fun getUser(
        userId: Long = idCounter.getAndIncrement(),
        email: String = "${userId}@raggies.ru",
    ) = User(userId, email)

}