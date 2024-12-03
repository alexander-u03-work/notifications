package ru.raggies.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.security.cors")
data class SecurityProperties(
    val allowedOrigins: List<String>
)