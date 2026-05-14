package ru.raggies.notification.batch

import ru.raggies.notification.model.NotificationDeliveryType
import ru.raggies.notification.subscriptions.UserSubscription


data class NotificationBatchData(
    val userSubject: String,
    val email: String,
    val notificationName: String,
    val notificationType: NotificationDeliveryType,
    val messageArgs: Map<String, Any>
) {

    constructor(subscription: UserSubscription, messageArgs: Map<String, Any>) : this(
        subscription.userSubject,
        subscription.email,
        subscription.notification.name,
        subscription.notification.typeName,
        messageArgs
    )

}
