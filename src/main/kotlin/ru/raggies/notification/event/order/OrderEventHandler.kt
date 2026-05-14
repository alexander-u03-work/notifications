package ru.raggies.notification.event.order

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.event.NotificationEventHandler
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class OrderEventHandler(
    notificationJob: Job,
    jobLauncher: JobLauncher,
): NotificationEventHandler<OrderEvent>(
    notificationJob, jobLauncher
) {

    @RabbitListener(queues = ["\${app.rabbitmq.queues.orders-notification.queue-name}"])
    override fun handleEvent(event: Message<OrderEvent>) {
        super.handleEvent(event)
    }

    override fun getJobParameters(event: OrderEvent): JobParametersBuilder = JobParametersBuilder()
        .addString("notificationName", event.getNotificationName())
        .addLong("userId", event.source.userId)
        .addLong("orderId", event.source.id)
        .addLocalDateTime("deliveryDate", LocalDateTime.ofInstant(event.source.deliveryDate, ZoneOffset.systemDefault()))
        .addString("payment", event.source.payment)

}