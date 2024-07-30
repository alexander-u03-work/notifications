package ru.raggies.notification.notifications

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component
import ru.raggies.notification.notifications.processing.NotificationItemReader
import ru.raggies.notification.notifications.processing.NotificationSender

@Component
class NotificationListener(
    private val userNotificationManager: UserNotificationManager,
    private val notificationSender: NotificationSender,
    private val distributionNotificationListener: NotificationItemReader,
    private val jobLauncher: JobLauncher,
    private val notificationJob: Job,
) {

    @RabbitListener(queues = ["distribution-notifications"])
    fun notificationsListener(request: NotificationRequest) {
        distributionNotificationListener.handleNotification(request)

        val params = JobParametersBuilder()
            .addLong("uniqueness", System.nanoTime())
            .toJobParameters()
        jobLauncher.run(notificationJob, params)
    }

    @RabbitListener(queues = ["personal-notifications"])
    fun personalNotificationsListener(request: PersonalNotificationRequest) {
        val statistic = userNotificationManager.getUserByEmailAndNotificationName(request.notificationName, request.email) ?:
            return

        notificationSender.sendNotification(UserNotificationInfo(request, statistic))
    }

}
