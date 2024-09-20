package ru.raggies.notification

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.test.TestRabbitTemplate
import org.springframework.boot.fromApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.junit.jupiter.Testcontainers

@EnableRabbit
@Configuration
@Testcontainers
class TestNotificationApplication {

    @Bean
    fun testRabbitTemplate(connectionFactory: ConnectionFactory) =
        TestRabbitTemplate(connectionFactory)

}

fun main(args: Array<String>) {
    fromApplication<NotificationApplication>()
        .with(TestNotificationApplication::class.java).run(*args)
}
