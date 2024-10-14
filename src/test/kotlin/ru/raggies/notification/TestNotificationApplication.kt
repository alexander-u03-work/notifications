package ru.raggies.notification

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer

@EnableRabbit
@TestConfiguration(proxyBeanMethods = false)
class TestNotificationApplication {

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer("postgres:latest")
            .withReuse(true)
            .withDatabaseName("raggies-test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(Network.newNetwork())

    @Bean
    @ServiceConnection
    fun rabbitContainer(): RabbitMQContainer =
        RabbitMQContainer("rabbitmq:3.9-management")
            .withReuse(true)
            .withUser("admin", "admin")
            .withVhost("vhost")
            .withPermission("vhost", "admin", ".*", ".*", ".*")

}

fun main(args: Array<String>) {
    fromApplication<NotificationApplication>()
        .with(TestNotificationApplication::class.java)
        .run(*args)
}
