package ru.raggies.notification.notifications.batch

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.batch-processing")
data class BatchProcessingProperties(
    val chunkSize: Int,
    val retryLimit: Int,
    val queues: Queues
) {

    fun deadLetterQueueName() = queues.deadLetter

    fun jobProcessingQueueName() = queues.jobProcessing

    data class Queues(
        val deadLetter: String,
        val jobProcessing: String,
    )

}
