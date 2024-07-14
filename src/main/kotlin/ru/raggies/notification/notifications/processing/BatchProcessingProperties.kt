package ru.raggies.notification.notifications.processing

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.batch-processing")
data class BatchProcessingProperties(
    val chunkSize: Int,
    val retryLimit: Int,
)
