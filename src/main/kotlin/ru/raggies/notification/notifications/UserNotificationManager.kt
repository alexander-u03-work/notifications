package ru.raggies.notification.notifications

import org.jooq.impl.DSL
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.create
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.notifications.processing.UserNotificationStatistic
import ru.raggies.notification.subscribtions.SubscribedNotificationRequest
import ru.raggies.notification.subscribtions.SubscriptionNotificationInfo
import ru.raggies.notification.subscribtions.UserNotificationInfo
import ru.raggies.notification.tables.UserNotificationSubscription.Companion.USER_NOTIFICATION_SUBSCRIPTION
import ru.raggies.notification.tables.daos.UserNotificationDao
import ru.raggies.notification.tables.daos.UserNotificationSubscriptionDao
import ru.raggies.notification.tables.pojos.UserNotification
import ru.raggies.notification.tables.pojos.UserNotificationSubscription
import ru.raggies.notification.tables.references.NOTIFICATION
import ru.raggies.notification.tables.references.NOTIFICATION_TYPE
import ru.raggies.notification.tables.references.USER_NOTIFICATION
import java.time.Instant

@Service
class UserNotificationManager(
    private val notificationManager: NotificationManager,
    private val userNotificationSubscriptionDao: UserNotificationSubscriptionDao,
    private val userNotificationDao: UserNotificationDao,
) {

    fun createUserNotification(
         statistic: UserNotificationStatistic
    ): UserNotificationInfo {
        val userNotification = userNotificationDao.create(UserNotification(
            userId = statistic.userId,
            notificationName = statistic.notificationName,
            message = statistic.message(),
            sentAt = Instant.now()
        ))
        return UserNotificationInfo(userNotification)
    }

    fun getUserNotifications(userId: Long): List<UserNotificationInfo> = userNotificationDao.ctx()
        .selectFrom(USER_NOTIFICATION)
        .where(USER_NOTIFICATION.USER_ID.eq(userId))
        .orderBy(USER_NOTIFICATION.SENT_AT)
        .fetch { UserNotificationInfo(it.value1()!!, it.value2()!!, it.value3()!!, it.value4().toString(), it.value5()!!) }

    @Transactional
    fun subscribeUserForNotificationType(
        userId: Long,
        request: SubscribedNotificationRequest
    ): UserNotificationSubscription {
        val notification = notificationManager.getNotificationByNameAndDeliveryType(request.name, request.deliveryType) ?:
            throw ApplicationException("Notification with name ${request.name} not found")

        val isUserSubscribed = isUserSubscribedForNotificationType(userId, request.name, request.deliveryType)
        if (request.subscribe && !isUserSubscribed) {
            return userNotificationSubscriptionDao.create(UserNotificationSubscription(
                userId = userId,
                notificationId = notification.id,
                email = request.email,
                active = true
            ))
        }

        if (!request.subscribe && isUserSubscribed) {
            return unsubscribeUser(userId, request.name, request.deliveryType) ?:
                throw ApplicationException("Can not unsubscribe user with userId $userId from required notification ${request.name}")
        }
        throw ApplicationException("Can not subscribe or unsubscribe for notifications ${request.name} ${request.deliveryType} user with id $userId")
    }

    fun getUserByEmailAndNotificationName(request: PersonalNotificationRequest): UserNotificationStatistic? =
        userNotificationSubscriptionDao.ctx()
            .select(
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL,
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID,
                DSL.arrayAgg(NOTIFICATION_TYPE.NAME)
            )
            .from(USER_NOTIFICATION_SUBSCRIPTION)
            .join(NOTIFICATION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(NOTIFICATION.NAME.eq(request.notificationName))
                .and(USER_NOTIFICATION_SUBSCRIPTION.EMAIL.eq(request.email))
            .groupBy(
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL,
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID
            )
            .fetchOne {
                UserNotificationStatistic(request.notificationName, request.args, it.value1()!!, it.value2()!!, it.value3().map { e -> e!! })
            }

    fun getUsersByNotificationName(request: NotificationRequest): List<UserNotificationStatistic> =
        userNotificationSubscriptionDao.ctx()
            .select(
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL,
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID,
                DSL.arrayAgg(NOTIFICATION_TYPE.NAME)
            )
            .from(USER_NOTIFICATION_SUBSCRIPTION)
            .join(NOTIFICATION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(NOTIFICATION.NAME.eq(request.notificationName))
            .groupBy(
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL,
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID
            )
            .fetch {
                UserNotificationStatistic(request.notificationName, request.args, it.value1()!!, it.value2()!!, it.value3().map { e -> e!! })
            }

    fun getUserNotificationTypesForUser(userId: Long): List<SubscriptionNotificationInfo> =
        userNotificationSubscriptionDao.ctx()
            .select(NOTIFICATION_TYPE.ID, NOTIFICATION_TYPE.NAME, DSL.array(NOTIFICATION_TYPE.NAME))
            .from(NOTIFICATION_TYPE)
            .join(USER_NOTIFICATION_SUBSCRIPTION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION_TYPE.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(USER_NOTIFICATION_SUBSCRIPTION.USER_ID.eq(userId))
            .groupBy(USER_NOTIFICATION_SUBSCRIPTION.USER_ID)
            .fetchInto(SubscriptionNotificationInfo::class.java)

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
    ): UserNotificationSubscription? = userNotificationSubscriptionDao.ctx()
        .update(USER_NOTIFICATION_SUBSCRIPTION)
        .set(USER_NOTIFICATION_SUBSCRIPTION.ACTIVE, false)
        .where(USER_NOTIFICATION_SUBSCRIPTION.USER_ID.eq(userId))
            .and(NOTIFICATION.NAME.eq(name))
            .and(NOTIFICATION_TYPE.NAME.eq(deliveryType))
            .and(NOTIFICATION.IS_REQUIRED.eq(false))
        .returning()
        .fetchOneInto(UserNotificationSubscription::class.java)

}