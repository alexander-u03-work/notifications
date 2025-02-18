package ru.raggies.notification.notifications

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(NotificationsController.BASE_URL)
class NotificationsController(
    private val notificationManager: NotificationManager
) {

    companion object {
        const val BASE_URL = "/api/notifications"
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    fun createNotification(@RequestBody request: CreateNotificationRequest): Notification =
        notificationManager.createNotification(request)

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @GetMapping
    fun getNotifications(): List<Notification> = notificationManager.getNotifications()

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @GetMapping("types")
    fun getNotificationTypes(): List<NotificationType> = notificationManager.getNotificationTypes()

}