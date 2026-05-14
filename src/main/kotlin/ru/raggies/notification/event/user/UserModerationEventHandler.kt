package ru.raggies.notification.event.user

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.event.NotificationEventHandler

@Component
class UserModerationEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<UserModerationEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = [$$"${app.rabbitmq.queues.users-moderation.queue-name}"])
    override fun handleEvent(event: Message<UserModerationEvent>) {
        super.handleEvent(event)
    }

    override fun getJobParameters(event: UserModerationEvent): JobParametersBuilder {
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addString("userSubject", event.source.subject)

        event.issueType?.let {
            builder.addString("issueType", it)
        }
        event.comment?.let {
            builder.addString("comment", it)
        }

        return builder
    }
}