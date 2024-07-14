package ru.raggies.notification.notifications.processing

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.UserNotificationManager
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@Component
class NotificationItemReader(
    private val userNotificationManager: UserNotificationManager,
    private var results: AtomicReference<MutableList<UserNotificationStatistic>> = AtomicReference(mutableListOf()),
    private var current: AtomicInteger = AtomicInteger()
): ItemReader<UserNotificationStatistic> {

    fun handleNotification(request: NotificationRequest) {
        results.getAndUpdate { r ->
            r.addAll(userNotificationManager.getUsersByNotificationName(request))
            r
        }
    }

    override fun read(): UserNotificationStatistic? {
        val next = current.getAndIncrement()

        return if (next < results.get().size) {
            results.get()[next]
        } else {
            null
        }
    }
}