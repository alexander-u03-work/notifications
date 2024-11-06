package ru.raggies.notification

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer

@EnableRabbit
@TestConfiguration(proxyBeanMethods = false)
class TestNotificationApplication {

    private val network = Network.newNetwork()

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer("postgres:latest")
            .withReuse(true)
            .withDatabaseName("raggies-test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(network)

    @Bean
    fun keycloakContainer(): KeycloakContainer =
        KeycloakContainer("keycloak/keycloak:25.0.2")
            .withRealmImportFile("realm.json")
            .withNetwork(network)

    @Bean
    @ServiceConnection
    fun rabbitContainer(): RabbitMQContainer =
        RabbitMQContainer("rabbitmq:3.9-management")
            .withReuse(true)
            .withUser("admin", "admin")
            .withVhost("vhost")
            .withPermission("vhost", "admin", ".*", ".*", ".*")

    companion object {
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry, postgresContainer: PostgreSQLContainer<*>, keycloakContainer: KeycloakContainer) {
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri") { keycloakContainer.authServerUrl + "/realms/raggies-realm" }
            registry.add("spring.security.oauth2.resourceserver.jwt.token-uri") { keycloakContainer.authServerUrl + "/realms/raggies-realm" }
        }
    }

}

fun main(args: Array<String>) {
    fromApplication<NotificationApplication>()
        .with(TestNotificationApplication::class.java)
        .run(*args)
}
