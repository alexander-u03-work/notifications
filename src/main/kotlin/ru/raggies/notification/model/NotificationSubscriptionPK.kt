package ru.raggies.notification.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class NotificationSubscriptionPK(
    @Column
    val userId: Long? = null,
    @Column
    val notificationId: Long? = null
): Serializable