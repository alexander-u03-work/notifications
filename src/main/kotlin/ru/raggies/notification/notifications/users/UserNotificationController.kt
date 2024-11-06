package ru.raggies.notification.notifications.users

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.raggies.notification.notifications.NotificationManager
import ru.raggies.notification.notifications.NotificationProperties
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.PersonalNotificationRequest

@RestController
@RequestMapping(UserNotificationController.BASE_URL)
class UserNotificationController(
    private val userNotificationManager: NotificationManager,
    private val rabbitTemplate: RabbitTemplate,
    private val properties: NotificationProperties
) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @PostMapping
    fun distributionNotification(@RequestBody request: NotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.distributionQueueName(), request)
        return ResponseEntity.ok().body(request.messageArgs)
    }

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @PostMapping("/personal")
    fun personalNotification(@RequestBody request: PersonalNotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.personalQueueName(),  request)
        return ResponseEntity.ok().body(request.messageArgs)
    }

    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    fun getUsersNotifications(@PathVariable userId: Long): List<UserNotification> =
        userNotificationManager.getUserNotifications(userId)
}