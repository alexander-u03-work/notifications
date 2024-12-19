package ru.raggies.notification.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import ru.raggies.notification.notifications.batch.BatchProcessingProperties
import ru.raggies.notification.notifications.batch.NotificationProcessRequest
import ru.raggies.notification.notifications.batch.NotificationRequestProvider
import ru.raggies.notification.notifications.sending.NotificationDeliveryProvider

@EnableConfigurationProperties(value = [BatchProperties::class])
@Configuration
class BatchConfiguration(
    private val processingProperties: BatchProcessingProperties,
) {

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