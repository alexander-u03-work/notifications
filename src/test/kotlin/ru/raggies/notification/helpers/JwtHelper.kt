package ru.raggies.notification.helpers

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtHelper {
    fun generateJwt(subject: String, seconds: Long): Jwt {
        val now = Instant.now()
        return Jwt.withTokenValue("token")
            .issuedAt(now)
            .expiresAt(now.plus(seconds, ChronoUnit.SECONDS))
            .header("alg", "none")
            .claim("sub", subject)
            .build()
    }
}