package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.notifications.NotificationType

@Entity
@Table(name = "notification_type", schema = "notification_schema")
class NotificationTypeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val name: NotificationDeliveryType? = null
) {

    fun toDTO() = NotificationType(id!!, name!!)

}