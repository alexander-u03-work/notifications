package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.notifications.users.UserNotification
import java.time.Instant

@Entity
@Table(name = "user_notification", schema = "notification_schema")
class UserNotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var userId: Long? = null,

    @Column(nullable = false)
    val notificationName: String? = null,

    @Column(nullable = false)
    val message: String? = null,

    @Column(nullable = false)
    val sentAt: Instant? = null
) {
    fun toDTO() = UserNotification(id!!, userId!!, notificationName!!, message!!, sentAt!!)
}