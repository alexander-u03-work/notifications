package ru.raggies.notification.subscriptions

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.model.NotificationSubscriptionPK


interface NotificationSubscriptionRepository: JpaRepository<NotificationSubscriptionEntity, NotificationSubscriptionPK> {

    fun findByUserIdAndNotificationId(userId: Long, notificationId: Long): NotificationSubscriptionEntity?

    fun findByUserId(userId: Long): List<NotificationSubscriptionEntity>

}