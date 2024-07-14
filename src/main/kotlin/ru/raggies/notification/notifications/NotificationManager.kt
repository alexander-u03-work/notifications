package ru.raggies.notification.notifications

import org.springframework.stereotype.Service
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.tables.daos.NotificationDao
import ru.raggies.notification.tables.pojos.Notification
import ru.raggies.notification.tables.references.NOTIFICATION
import ru.raggies.notification.tables.references.NOTIFICATION_TYPE

@Service
class NotificationManager(
    private val notificationDao: NotificationDao,
) {

    fun getNotificationByNameAndDeliveryType(name: String, deliveryType: NotificationDeliveryType): Notification? = notificationDao.ctx()
        .select(NOTIFICATION)
        .from(NOTIFICATION)
        .join(NOTIFICATION_TYPE)
            .on(NOTIFICATION_TYPE.ID.eq(NOTIFICATION.NOTIFICATION_TYPE_ID))
        .where(NOTIFICATION.NAME.eq(name))
        .and(NOTIFICATION_TYPE.NAME.eq(deliveryType))
        .fetchOneInto(Notification::class.java)

    fun getNotificationTypes(): List<Notification> = notificationDao.findAll()

}