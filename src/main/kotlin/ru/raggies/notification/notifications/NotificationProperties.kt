package ru.raggies.notification.notifications

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.notifications")
data class NotificationProperties(
    val exchange: String,
    val queues: Queues,
    val concurrentConsumers: Int,
) {

    data class Queues(
        val deadLetterQueue: String,
        val personal: QueueProperties,
        val distribution: QueueProperties
    ) {

        data class QueueProperties(
            val routingKey: String,
            val queueName: String,
        )
    }

    fun personalQueueName() = queues.personal.queueName

    fun personalRoutingKey() = queues.personal.routingKey

    fun distributionQueueName() = queues.distribution.queueName

    fun distributionRoutingKey() = queues.distribution.routingKey

    fun deadLetterQueueName() = queues.deadLetterQueue

}
