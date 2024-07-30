package ru.raggies.notification.notifications

import org.jooq.impl.DSL
import org.springframework.stereotype.Service
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.subscribtions.NotificationInfo
import ru.raggies.notification.subscribtions.UserNotificationSubscriptionType
import ru.raggies.notification.tables.UserNotificationSubscription.Companion.USER_NOTIFICATION_SUBSCRIPTION
import ru.raggies.notification.tables.daos.UserNotificationDao
import ru.raggies.notification.tables.daos.UserNotificationSubscriptionDao
import ru.raggies.notification.tables.references.NOTIFICATION
import ru.raggies.notification.tables.references.NOTIFICATION_TYPE
import ru.raggies.notification.tables.references.USER_NOTIFICATION

@Service
class UserNotificationManager(
    private val userNotificationSubscriptionDao: UserNotificationSubscriptionDao,
    private val userNotificationDao: UserNotificationDao,
) {

    fun getUserNotifications(userId: Long): List<NotificationInfo> = userNotificationDao.ctx()
        .selectFrom(USER_NOTIFICATION)
        .where(USER_NOTIFICATION.USER_ID.eq(userId))
        .orderBy(USER_NOTIFICATION.SENT_AT)
        .fetch { NotificationInfo(it.value1()!!, it.value2()!!, it.value3()!!, it.value4().toString(), it.value5()!!) }

    fun getUserByEmailAndNotificationName(notificationName: String, email: String): PersonalNotificationStatistic? =
        userNotificationSubscriptionDao.ctx()
            .select(
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID,
                DSL.arrayAgg(NOTIFICATION_TYPE.NAME).`as`("types")
            )
            .from(USER_NOTIFICATION_SUBSCRIPTION)
            .join(NOTIFICATION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(NOTIFICATION.NAME.eq(notificationName))
                .and(USER_NOTIFICATION_SUBSCRIPTION.EMAIL.eq(email))
            .groupBy(
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID
            )
            .fetchOneInto(PersonalNotificationStatistic::class.java)

    fun getUsersByNotificationName(notificationName: String): List<NotificationStatistic> =
        userNotificationSubscriptionDao.ctx()
            .select(
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID,
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL,
                DSL.arrayAgg(NOTIFICATION_TYPE.NAME)
            )
            .from(USER_NOTIFICATION_SUBSCRIPTION)
            .join(NOTIFICATION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(NOTIFICATION.NAME.eq(notificationName))
            .groupBy(
                USER_NOTIFICATION_SUBSCRIPTION.USER_ID,
                USER_NOTIFICATION_SUBSCRIPTION.EMAIL
            )
            .fetchInto(NotificationStatistic::class.java)

    fun getUserNotificationTypesForUser(userId: Long): List<UserNotificationSubscriptionType> =
        userNotificationSubscriptionDao.ctx()
            .select(NOTIFICATION_TYPE.ID, NOTIFICATION_TYPE.NAME, DSL.array(NOTIFICATION_TYPE.NAME))
            .from(NOTIFICATION_TYPE)
            .join(USER_NOTIFICATION_SUBSCRIPTION)
                .on(USER_NOTIFICATION_SUBSCRIPTION.NOTIFICATION_ID.eq(NOTIFICATION_TYPE.ID))
            .join(NOTIFICATION_TYPE)
                .on(NOTIFICATION.NOTIFICATION_TYPE_ID.eq(NOTIFICATION_TYPE.ID))
            .where(USER_NOTIFICATION_SUBSCRIPTION.USER_ID.eq(userId))
            .groupBy(USER_NOTIFICATION_SUBSCRIPTION.USER_ID)
            .fetchInto(UserNotificationSubscriptionType::class.java)

    data class NotificationStatistic(
        val userId: Long,
        val email: String,
        val types: List<NotificationDeliveryType>
    )

    data class PersonalNotificationStatistic(
        val userId: Long,
        val types: List<NotificationDeliveryType>
    )

}