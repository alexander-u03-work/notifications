package ru.raggies.notification.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.exception.ExceptionHandler
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import ru.raggies.notification.batch.NotificationBatchData
import ru.raggies.notification.batch.UserNotificationSender
import ru.raggies.notification.batch.UserSubscriptionsReader
import ru.raggies.notification.events.DefaultJobParamsExtractor
import ru.raggies.notification.events.NotificationEventJobParamsExtractor
import ru.raggies.notification.subscriptions.SubscriptionManager


@EnableConfigurationProperties(value = [BatchProcessingProperties::class])
@Configuration
class BatchConfiguration(
    private val properties: BatchProcessingProperties
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
        taskExecutor: TaskExecutor,
        reader: UserSubscriptionsReader,
        sender: UserNotificationSender,
        exceptionHandler: ExceptionHandler,
    ): Step = StepBuilder("sendingNotification", jobRepository)
        .chunk<NotificationBatchData, NotificationBatchData>(properties.chunkSize, transactionManager)
        .reader(reader)
        .writer(sender)
        .taskExecutor(taskExecutor)
        .faultTolerant()
        .retryLimit(properties.retryLimit)
        .retry(Exception::class.java)
        .exceptionHandler(exceptionHandler)
        .build()

    @Bean
    @StepScope
    fun userSubscriptionsReader(
        @Value("#{stepExecution.jobParameters}")
        jobParameters: JobParameters,
        subscriptionManager: SubscriptionManager,
        defaultJobParamsExtractor: DefaultJobParamsExtractor,
        paramsExtractors: List<NotificationEventJobParamsExtractor>
    ): UserSubscriptionsReader {
        val reader = UserSubscriptionsReader(jobParameters, subscriptionManager, defaultJobParamsExtractor, paramsExtractors)
        reader.isSaveState = false
        reader.name = "sendingNotification"

        return reader
    }

    @Bean
    fun taskExecutor(): TaskExecutor {
        val executor = SimpleAsyncTaskExecutor("spring-batch-job-")
        executor.concurrencyLimit = 3

        return executor
    }

    @Bean
    fun exceptionHandler() = SimpleLimitExceptionHandler()

}