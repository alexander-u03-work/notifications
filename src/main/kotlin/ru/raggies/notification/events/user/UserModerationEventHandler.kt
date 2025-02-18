package ru.raggies.notification.events.user

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.events.NotificationEventHandler

@Component
class UserModerationEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<UserModerationEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = ["\${app.rabbitmq.queues.users-moderation.queue-name}"])
    override fun handleEvent(message: Message<UserModerationEvent>) {
        super.handleEvent(message)
    }

    override fun getJobParameters(event: UserModerationEvent): JobParametersBuilder {
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addLong("userId", event.source.id)

        event.issueType?.let {
            builder.addString("issueType", it)
        }
        event.comment?.let {
            builder.addString("comment", it)
        }

        return builder
    }
}