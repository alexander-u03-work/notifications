package ru.raggies.notification.subscribtions

import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.createAll
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.notifications.NotificationManager
import ru.raggies.notification.tables.UserNotificationSubscription.Companion.USER_NOTIFICATION_SUBSCRIPTION
import ru.raggies.notification.tables.daos.UserNotificationSubscriptionDao
import ru.raggies.notification.tables.pojos.UserNotificationSubscription
import ru.raggies.notification.tables.references.NOTIFICATION
import ru.raggies.notification.tables.references.NOTIFICATION_TYPE

@Component
class UserSubscriptionManager(
    private val notificationManager: NotificationManager,
    private val userNotificationSubscriptionDao: UserNotificationSubscriptionDao
) {

    @Transactional
    fun subscribeUserForNotifications(request: SubscribeOnRegisterRequest) {
        userNotificationSubscriptionDao.createAll(
            notificationManager.getAllNotifications()
                .map { notification ->
                    UserNotificationSubscription(request.userId, notification.id, request.email, true)
                }
        )
    }

    @Transactional
    fun unsubscribeUserFromNotifications(userId: Long, request: UnsubscribeUserRequest): UserNotificationSubscription {
        if ( !isUserSubscribedForNotificationType(userId, request.notificationName, request.deliveryType)) {
            throw ApplicationException("Can not unsubscribe for notifications ${request.notificationName} ${request.deliveryType} user with id $userId")
        }
        return unsubscribeUser(userId, request.notificationName, request.deliveryType)
    }

    private fun isUserSubscribedForNotificationType(
        userId: Long,
        notificationName: String,
        notificationType: NotificationDeliveryType
    ): Boolean = userNotificationSubscriptionDao.ctx()
        .fetchExists(
            DSL.select(USER_NOTIFICATION_SUBSCRIPTION)
                .from(USER_NOTIFICATION_SUBSCRIPTION)
                .join(NOTIFICATION)
                .on(NOTIFICATION.ID.eq(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID))
                .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
                .where(USER_NOTIFICATION_SUBSCRIPTION.USER_ID.eq(userId)
                    .and(NOTIFICATION.NAME.eq(notificationName))
                    .and(NOTIFICATION_TYPE.NAME.eq(notificationType))
                )
        )

    private fun unsubscribeUser(
        userId: Long,
        name: String,
        deliveryType: NotificationDeliveryType
    ): UserNotificationSubscription = userNotificationSubscriptionDao.ctx()
        .update(USER_NOTIFICATION_SUBSCRIPTION)
        .set(USER_NOTIFICATION_SUBSCRIPTION.ACTIVE, false)
        .where(USER_NOTIFICATION_SUBSCRIPTION.USER_ID.eq(userId))
        .and(NOTIFICATION.NAME.eq(name))
        .and(NOTIFICATION_TYPE.NAME.eq(deliveryType))
        .and(NOTIFICATION.IS_REQUIRED.eq(false))
        .returning()
        .fetchOneInto(UserNotificationSubscription::class.java)!!

}