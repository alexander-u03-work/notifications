package ru.raggies.notification.notifications

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.notifications")
data class NotificationProperties(
    val exchange: String,
    val queues: Queues,
    val concurrentConsumers: Int,
) {

    data class Queues(
        val personal: String,
        val distribution: String,
        val subscribeOnRegister: String,
    )

    fun personalQueueName() = queues.personal

    fun distributionQueueName() = queues.distribution

    fun subscribeOnRegisterQueueName() = queues.subscribeOnRegister

}
