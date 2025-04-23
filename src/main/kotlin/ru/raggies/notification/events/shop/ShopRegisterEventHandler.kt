package ru.raggies.notification.events.shop

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
class ShopRegisterEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<ShopRegisterEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = ["\${app.rabbitmq.queues.shops-registration.queue-name}"])
    override fun handleEvent(message: Message<ShopRegisterEvent>) {
        super.handleEvent(message)
    }

    override fun getJobParameters(event: ShopRegisterEvent): JobParametersBuilder {
        val suggestion = event.source.suggestion ?: ""
        val builder = JobParametersBuilder()
            .addString("notificationName", event.getNotificationName())
            .addString("username", event.source.username)
            .addString("shopName", event.source.shopName)
            .addString("phoneNumber", event.source.phoneNumber)
            .addString("suggestion", suggestion)
            .addJobParameter("adminIds", event.adminIds, List::class.java)

        return builder
    }
}