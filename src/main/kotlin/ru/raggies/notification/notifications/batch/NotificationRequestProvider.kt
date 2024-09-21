package ru.raggies.notification.notifications.batch

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Service
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.PersonalNotificationRequest
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import ru.raggies.notification.subscribtions.SubscriptionManager
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@Service
class NotificationRequestProvider(
    private val userNotificationManager: SubscriptionManager,
    private val rabbitTemplate: RabbitTemplate,
    private val processingProperties: BatchProcessingProperties,
    private var notificationsBatch: AtomicReference<MutableList<NotificationProcessRequest>> = AtomicReference(mutableListOf()),
    private var current: AtomicInteger = AtomicInteger(),
): ItemReader<NotificationProcessRequest> {

    fun handleDistrictNotification(request: NotificationRequest) {
        val toProcess = userNotificationManager.getSubscribedNotifications(request.notificationName)

        processNotificationInfo(toProcess, request.messageArgs)
    }

    fun handlePersonalNotification(request: PersonalNotificationRequest) {
        val toProcess = userNotificationManager.getSubscribedNotifications(request.userId, request.notificationName)

        processNotificationInfo(toProcess, request.messageArgs)
    }

    override fun read(): NotificationProcessRequest? {
        val next = current.getAndIncrement()

        return if (next < notificationsBatch.get().size) {
            notificationsBatch.get()[next]
        } else {
            null
        }
    }

    private fun processNotificationInfo(toProcess: List<UserNotificationSubscription>, messageArgs: Map<String, String>) {
        notificationsBatch.getAndUpdate { r ->
            r.addAll(toProcess.map { NotificationProcessRequest(it, messageArgs) })
            r
        }

        if (notificationsBatch.get().size == processingProperties.chunkSize) {
            val request = JobStartRequest(1, Instant.now())
            rabbitTemplate.convertAndSend(processingProperties.jobProcessingQueueName(), request)
        }
    }

}