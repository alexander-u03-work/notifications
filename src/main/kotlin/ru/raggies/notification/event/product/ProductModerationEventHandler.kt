package ru.raggies.notification.event.product

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.event.NotificationEventHandler

@Component
class ProductModerationEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<ProductModerationEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = [$$"${app.rabbitmq.queues.products-moderation.queue-name}"])
    override fun handleEvent(event: Message<ProductModerationEvent>) {
        super.handleEvent(event)
    }

    override fun getJobParameters(event: ProductModerationEvent): JobParametersBuilder {
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addJobParameter("userSubjects", event.shopOwners, List::class.java)
            .addLong("productId", event.source.id)
            .addString("productTitle", event.source.title)
            .addString("productUrl", event.productUrl)

        event.issueType?.let {
            builder.addString("issueType", it)
        }
        event.comment?.let {
            builder.addString("comment", it)
        }

        return builder
    }
}