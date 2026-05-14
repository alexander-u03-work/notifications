package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.event.user.User
import ru.raggies.notification.notifications.Notification
import ru.raggies.notification.subscriptions.UserSubscription


@Entity
@Table(name = "user_notification_subscription", schema = "notification_schema")
@IdClass(NotificationSubscriptionPK::class)
class NotificationSubscriptionEntity(
    @Id
    @Column
    val userSubject: String? = null,

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

    constructor(user: User, notification: Notification, active: Boolean): this(
        user.subject,
        notification.id,
        NotificationEntity(notification),
        user.username,
        active
    )

    fun toDTO() = UserSubscription(userSubject!!, email!!, notification?.toDTO()!!, active!!)
}