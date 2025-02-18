package ru.raggies.notification.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(value = [RabbitMqProperties::class])
@Configuration
class RabbitMqConfiguration(
    private val properties: RabbitMqProperties,
) {

    @Bean
    fun usersTopicExchange() =
        TopicExchange(properties.exchanges.users)

    @Bean
    fun ordersTopicExchange() =
        TopicExchange(properties.exchanges.orders)

    @Bean
    fun productsTopicExchange() =
        TopicExchange(properties.exchanges.products)

    @Bean
    fun usersModerationQueue(): Queue = QueueBuilder
        .durable(properties.queues.usersModeration.queueName)
        .ttl(properties.queues.usersModeration.ttl)
        .maxLength(properties.queues.usersModeration.maxLength)
        .deadLetterExchange(properties.exchanges.dlx)
        .overflow(QueueBuilder.Overflow.rejectPublish)
        .build()

    @Bean
    fun usersRegisterQueue(): Queue = QueueBuilder
        .durable(properties.queues.usersRegistration.queueName)
        .ttl(properties.queues.usersRegistration.ttl)
        .maxLength(properties.queues.usersRegistration.maxLength)
        .deadLetterExchange(properties.exchanges.dlx)
        .overflow(QueueBuilder.Overflow.rejectPublish)
        .build()

    @Bean
    fun productsModerationQueue(): Queue = QueueBuilder
        .durable(properties.queues.productsModeration.queueName)
        .ttl(properties.queues.productsModeration.ttl)
        .maxLength(properties.queues.productsModeration.maxLength)
        .deadLetterExchange(properties.exchanges.dlx)
        .overflow(QueueBuilder.Overflow.rejectPublish)
        .build()

    @Bean
    fun ordersNotificationQueue(): Queue = QueueBuilder
        .durable(properties.queues.ordersNotification.queueName)
        .ttl(properties.queues.ordersNotification.ttl)
        .maxLength(properties.queues.ordersNotification.maxLength)
        .deadLetterExchange(properties.exchanges.dlx)
        .overflow(QueueBuilder.Overflow.rejectPublish)
        .build()

    @Bean
    fun usersModerationBinding(
        usersModerationQueue: Queue,
        usersTopicExchange: TopicExchange,
    ): Declarables {
        val bindings = properties.queues.usersModeration.routingKeys.map { routingKey ->
            BindingBuilder
                .bind(usersModerationQueue)
                .to(usersTopicExchange)
                .with(routingKey)
        }

        return Declarables(bindings)
    }

    @Bean
    fun usersRegisterBinding(
        usersRegisterQueue: Queue,
        usersTopicExchange: TopicExchange,
    ): Binding = BindingBuilder
        .bind(usersRegisterQueue)
        .to(usersTopicExchange)
        .with(properties.queues.usersRegistration.routingKeys.firstOrNull())

    @Bean
    fun ordersBinding(
        ordersNotificationQueue: Queue,
        ordersTopicExchange: TopicExchange,
    ): Binding = BindingBuilder
        .bind(ordersNotificationQueue)
        .to(ordersTopicExchange)
        .with(properties.queues.ordersNotification.routingKeys.firstOrNull())

    @Bean
    fun productsBinding(
        productsModerationQueue: Queue,
        productsTopicExchange: TopicExchange,
    ): Declarables {
        val bindings = properties.queues.productsModeration.routingKeys.map { routingKey ->
            BindingBuilder
                .bind(productsModerationQueue)
                .to(productsTopicExchange)
                .with(routingKey)
        }

        return Declarables(bindings)
    }

    @Bean
    fun messageConverter(objectMapper: ObjectMapper) =
        Jackson2JsonMessageConverter(objectMapper)

}