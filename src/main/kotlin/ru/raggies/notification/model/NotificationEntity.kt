package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.notifications.Notification

@Entity
@Table(name = "notification", schema = "notification_schema")
class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type_id")
    val type: NotificationTypeEntity? = null,

    @Column(nullable = false)
    val isRequired: Boolean? = null
) {
    fun toDTO() = Notification(id!!, name!!, isRequired!!, type?.id!!, type.name!!)
}