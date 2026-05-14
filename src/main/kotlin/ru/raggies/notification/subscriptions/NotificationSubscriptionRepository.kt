package ru.raggies.notification.subscriptions

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.NotificationSubscriptionEntity
import ru.raggies.notification.model.NotificationSubscriptionPK


interface NotificationSubscriptionRepository: JpaRepository<NotificationSubscriptionEntity, NotificationSubscriptionPK> {

    fun findByUserSubjectAndNotificationId(userSubject: String, notificationId: Long): NotificationSubscriptionEntity?

    fun findByUserSubject(subject: String): List<NotificationSubscriptionEntity>

}