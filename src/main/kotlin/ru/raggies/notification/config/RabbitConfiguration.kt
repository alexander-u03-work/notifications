package ru.raggies.notification.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.raggies.notification.notifications.NotificationProperties
import ru.raggies.notification.notifications.batch.BatchProcessingProperties


@EnableConfigurationProperties(value = [BatchProcessingProperties::class, NotificationProperties::class])
@Configuration
class RabbitConfiguration(
    private val notificationProperties: NotificationProperties,
    private val processingProperties: BatchProcessingProperties,
) {

    @Bean
    fun personalNotificationsQueue() = Queue(notificationProperties.personalQueueName())

    @Bean
    fun subscribeOnRegisterQueue() = Queue(notificationProperties.subscribeOnRegisterQueueName())

    @Bean
    fun jobProcessingQueue() = Queue(processingProperties.jobProcessingQueueName())

    @Bean
    fun distributionNotificationsQueue() = Queue(notificationProperties.distributionQueueName())

    @Bean
    fun deadLetterQueue(): Queue =
        QueueBuilder
            .durable("${processingProperties.deadLetterQueueName()}.dlq")
            .build()

    @Bean
    fun messageConverter(objectMapper: ObjectMapper) =
        Jackson2JsonMessageConverter(objectMapper)

    @Bean
    fun rabbitErrorHandler() = ConditionalRejectingErrorHandler()

}