package ru.raggies.notification.notifications.users

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.raggies.notification.notifications.NotificationProperties
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.PersonalNotificationRequest

@RestController
@RequestMapping(UserNotificationController.BASE_URL)
class UserNotificationController(
    private val userNotificationManager: UserNotificationManager,
    private val rabbitTemplate: RabbitTemplate,
    private val properties: NotificationProperties
) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @PostMapping
    fun distributionNotification(@RequestBody request: NotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.distributionQueueName(), request)
        return ResponseEntity.ok().body(request.messageArgs)
    }

    @PostMapping("/personal")
    fun personalNotification(@RequestBody request: PersonalNotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.personalQueueName(),  request)
        return ResponseEntity.ok().body(request.messageArgs)
    }


    @GetMapping("/{userId}")
    fun getUsersNotifications(@PathVariable userId: Long): List<UserNotification> =
        userNotificationManager.getUserNotifications(userId)
}