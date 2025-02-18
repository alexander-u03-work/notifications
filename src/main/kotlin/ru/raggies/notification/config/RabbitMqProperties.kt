package ru.raggies.notification.config

import org.springframework.amqp.core.QueueBuilder
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.rabbitmq")
data class RabbitMqProperties(
    val concurrentConsumers: Int,
    val exchanges: Exchanges,
    val queues: Queues,
) {

    data class Queues(
        val usersModeration: QueueProps,
        val usersRegistration: QueueProps,
        val productsModeration: QueueProps,
        val ordersNotification: QueueProps,
    )

    data class Exchanges(
        val products: String,
        val users: String,
        val orders: String,
        val dlx: String,
    )

    data class QueueProps(
        val queueName: String,
        val routingKeys: List<String>,
        val maxLength: Long,
        val ttl: Int,
        val overflow: QueueBuilder.Overflow,
    )

}
