package ru.raggies.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class MailProperties(
    val mail: String
)
