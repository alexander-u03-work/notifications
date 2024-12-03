package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.notifications.Notification
import ru.raggies.notification.notifications.users.UserNotificationSubscription

@Entity
@Table(name = "user_notification_subscription", schema = "notification_schema")
@IdClass(NotificationSubscriptionPK::class)
class NotificationSubscriptionEntity(
    @Id
    @Column
    val userId: Long? = null,

    @Id
    @Column
    val notificationId: Long? = null,

    @MapsId("notificationId")
    @ManyToOne(fetch = FetchType.EAGER)
    val notification: NotificationEntity? = null,

    @Column(nullable = false)
    val email: String? = null,

    @Column(nullable = false)
    var active: Boolean? = null
) {

    constructor(userId: Long, notification: Notification, email: String, active: Boolean): this(
        userId,
        notification.id,
        NotificationEntity(notification),
        email,
        active
    )

    fun toDTO() = UserNotificationSubscription(userId!!, email!!, notification?.toDTO()!!, active!!)
}