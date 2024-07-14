package ru.raggies.notification.notifications

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.raggies.notification.tables.pojos.Notification

@RestController
@RequestMapping("/notifications")
class NotificationsController(
    private val notificationManager: NotificationManager,
    private val rabbitTemplate: RabbitTemplate,
    private val properties: NotificationProperties
) {

    @PostMapping("/users")
    fun notificateUsers(@RequestBody request: NotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.exchange, properties.distributionRoutingKey(), request)
        return ResponseEntity.ok().body(request.args)
    }

    @PostMapping("/users/personal")
    fun notificateUser(@RequestBody request: PersonalNotificationRequest): ResponseEntity<*> {
        rabbitTemplate.convertAndSend(properties.exchange, properties.personalRoutingKey(),  request)
        return ResponseEntity.ok().body(request.args)
    }

    @GetMapping("/types")
    fun getNotificationTypes(@RequestParam forUser: Boolean): List<Notification> =
        notificationManager.getNotificationTypes()

}