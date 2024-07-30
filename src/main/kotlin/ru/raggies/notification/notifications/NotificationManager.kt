package ru.raggies.notification.notifications

import org.springframework.stereotype.Service
import ru.raggies.notification.tables.daos.NotificationDao
import ru.raggies.notification.tables.pojos.Notification
import ru.raggies.notification.tables.references.NOTIFICATION
import ru.raggies.notification.tables.references.NOTIFICATION_TYPE

@Service
class NotificationManager(
    private val notificationDao: NotificationDao,
) {

    fun getAllNotifications(): List<Notification> = notificationDao.ctx()
        .select(NOTIFICATION)
        .from(NOTIFICATION)
        .join(NOTIFICATION_TYPE)
            .on(NOTIFICATION_TYPE.ID.eq(NOTIFICATION.NOTIFICATION_TYPE_ID))
        .fetchInto(Notification::class.java)

    fun getNotificationTypes(): List<Notification> = notificationDao.findAll()

}