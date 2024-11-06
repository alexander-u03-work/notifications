package ru.raggies.notification.notifications

import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @GetMapping
    fun getNotifications(): List<Notification> = notificationManager.getNotifications()

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @GetMapping("types")
    fun getNotificationTypes(): List<NotificationType> = notificationManager.getNotificationTypes()

}