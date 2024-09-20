package ru.raggies.notification.notifications.users

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.model.NotificationSubscriptionPK

interface NotificationSubscriptionRepository: JpaRepository<NotificationSubscriptionEntity, NotificationSubscriptionPK> {

    fun findAllByUserIdAndNotificationNameAndActive(userId: Long, notificationName: String, isActive: Boolean): List<NotificationSubscriptionEntity>

    fun findByUserIdAndNotificationId(userId: Long, notificationId: Long): NotificationSubscriptionEntity?

    fun findByUserId(userId: Long): List<NotificationSubscriptionEntity>

    fun findByNotificationNameAndActive(notificationName: String, isActive: Boolean): List<NotificationSubscriptionEntity>

}