package ru.raggies.notification

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile

@EnableRabbit
@TestConfiguration(proxyBeanMethods = false)
class TestNotificationApplication {

    private val network = Network.newNetwork()


    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("raggies-test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetworkAliases("postgres")
            .withExposedPorts(5432)

    @Bean
    fun casdoorContainer(): GenericContainer<*>
        = GenericContainer("casbin/casdoor-all-in-one:4.30.0")
            .withExposedPorts(8000)
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("casdoor/init_data.json"),
                "/casdoor/init_data.json"
            )
            .waitingFor(Wait.forHttp("/.well-known/openid-configuration").forStatusCode(200))



    @Bean
    @ServiceConnection
    fun rabbitContainer(): RabbitMQContainer =
        RabbitMQContainer("rabbitmq:management")
            .withAdminUser("notification-tests")
            .withAdminPassword("notification-tests")
            .withNetwork(network)

    @Bean
    fun casdoorProperties(
        casdoorContainer: GenericContainer<*>
    ): DynamicPropertyRegistrar =
        DynamicPropertyRegistrar { registry ->
            val casdoorUrl = {
                "http://${casdoorContainer.host}:${casdoorContainer.getMappedPort(8000)}"
            }

            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", casdoorUrl)
        }

}

fun main(args: Array<String>) {
    fromApplication<NotificationApplication>()
        .with(TestNotificationApplication::class.java)
        .run(*args)
}
