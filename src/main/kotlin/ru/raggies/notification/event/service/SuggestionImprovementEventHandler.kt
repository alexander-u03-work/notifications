package ru.raggies.notification.event.service

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.event.NotificationEventHandler

@Component
class SuggestionImprovementEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<SuggestionImprovementEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = [$$"${app.rabbitmq.queues.default-improvement-suggestion.queue-name}"])
    override fun handleEvent(event: Message<SuggestionImprovementEvent>) {
        super.handleEvent(event)
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
            .addJobParameter("adminSubjects", event.adminSubjects, List::class.java)

        return builder
    }
}