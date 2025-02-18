package ru.raggies.notification.batch.sending

import ru.raggies.notification.batch.NotificationBatchData
import ru.raggies.notification.model.NotificationDeliveryType


interface NotificationSender {

    fun getType(): NotificationDeliveryType

    fun sendNotification(data: NotificationBatchData, message: String)

}