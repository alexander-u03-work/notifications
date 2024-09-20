package ru.raggies.notification.notifications

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(NotificationsController.BASE_URL)
class NotificationsController(
    private val notificationManager: NotificationManager
) {

    companion object {
        const val BASE_URL = "/api/notifications"
    }

    @GetMapping
    fun getNotifications(): List<Notification> = notificationManager.getNotifications()

    @GetMapping("types")
    fun getNotificationTypes(): List<NotificationType> = notificationManager.getNotificationTypes()

}