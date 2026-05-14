package ru.raggies.notification.event.shop

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.event.NotificationEventHandler

@Component
class ShopRegisterEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<ShopRegisterEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = [$$"${app.rabbitmq.queues.shops-registration.queue-name}"])
    override fun handleEvent(event: Message<ShopRegisterEvent>) {
        super.handleEvent(event)
    }

    override fun getJobParameters(event: ShopRegisterEvent): JobParametersBuilder {
        val suggestion = event.source.suggestion ?: ""
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addString("username", event.source.username)
            .addString("shopName", event.source.shopName)
            .addString("phoneNumber", event.source.phoneNumber)
            .addString("suggestion", suggestion)
            .addJobParameter("adminSubjects", event.adminSubjects, List::class.java)

        return builder
    }
}