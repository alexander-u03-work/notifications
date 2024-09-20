package ru.raggies.notification

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import ru.raggies.notification.notifications.NotificationProperties
import ru.raggies.notification.notifications.batch.BatchProcessingProperties
import ru.raggies.notification.notifications.batch.NotificationProcessRequest
import ru.raggies.notification.notifications.batch.NotificationRequestProvider
import ru.raggies.notification.notifications.sending.NotificationDeliveryProvider


@EnableBatchProcessing
@EnableConfigurationProperties(value = [BatchProcessingProperties::class, NotificationProperties::class])
@Configuration
class ModuleConfiguration(
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

    /*@Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .authorizeRequests { requests ->
                requests.anyRequest().authenticated()
            }
            .oauth2ResourceServer { resource -> resource.jwt(Customizer.withDefaults()) }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()

    @Bean
    fun jwtAuthoritiesConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setPrincipalClaimName("preferred_username")
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = jwt.getClaimAsMap("realm_access")["roles"] as List<String>

            roles
                .filter { role -> role.startsWith("role_") }
                .map { role -> role.uppercase(Locale.getDefault()) }
                .map { role -> SimpleGrantedAuthority(role) }
        }
        return converter
    }*/

    @Bean
    fun notificationJob(jobRepository: JobRepository, notificationStep: Step): Job =
        JobBuilder("notificationJob", jobRepository)
            .start(notificationStep)
            .build()

    @Bean
    fun notificationStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        notificationProvider: NotificationRequestProvider,
        notificationSender: NotificationDeliveryProvider,
    ): Step = StepBuilder("sendingNotification", jobRepository)
        .chunk<NotificationProcessRequest, NotificationProcessRequest>(processingProperties.chunkSize, transactionManager)
        .reader(notificationProvider)
        .writer(notificationSender)
        .build()

}