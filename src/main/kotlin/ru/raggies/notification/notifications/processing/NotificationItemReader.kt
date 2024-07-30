package ru.raggies.notification.notifications.processing

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.UserNotificationInfo
import ru.raggies.notification.notifications.UserNotificationManager
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@Component
class NotificationItemReader(
    private val userNotificationManager: UserNotificationManager,
    private var results: AtomicReference<MutableList<UserNotificationInfo>> = AtomicReference(mutableListOf()),
    private var current: AtomicInteger = AtomicInteger()
): ItemReader<UserNotificationInfo> {

    fun handleNotification(request: NotificationRequest) {
        results.getAndUpdate { r ->
            r.addAll(userNotificationManager.getUsersByNotificationName(request.notificationName)
                .map { UserNotificationInfo(request, it) })
            r
        }
    }

    override fun read(): UserNotificationInfo? {
        val next = current.getAndIncrement()

        return if (next < results.get().size) {
            results.get()[next]
        } else {
            null
        }
    }
}