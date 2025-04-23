package ru.raggies.notification.events.service

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.events.NotificationEventHandler
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class SuggestionImprovementEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<SuggestionImprovementEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = ["\${app.rabbitmq.queues.default-improvement-suggestion.queue-name}"])
    override fun handleEvent(message: Message<SuggestionImprovementEvent>) {
        super.handleEvent(message)
    }

    override fun getJobParameters(event: SuggestionImprovementEvent): JobParametersBuilder {
        val suggestion = event.source.suggestion ?: ""
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addString("username", event.source.username)
            .addString("phoneNumber", event.source.phoneNumber)
            .addLong("clientSatisfaction", event.source.clientSatisfaction)
            .addLong("interfaceComfort", event.source.interfaceComfort)
            .addLong("serviceWorkComfort", event.source.serviceWorkComfort)
            .addString("suggestion", suggestion)
            .addJobParameter("adminIds", event.adminIds, List::class.java)

        return builder
    }
}