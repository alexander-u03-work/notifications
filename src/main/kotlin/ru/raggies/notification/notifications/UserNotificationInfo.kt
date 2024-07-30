package ru.raggies.notification.notifications

import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.notifications.UserNotificationManager.*

data class UserNotificationInfo(
    val notificationName: String,
    val args: Map<String, String>,
    val userId: Long,
    val email: String,
    val types: List<NotificationDeliveryType>
) {

    constructor(request: NotificationRequest, statistic: NotificationStatistic):
            this(request.notificationName, request.args, statistic.userId, statistic.email, statistic.types)

    constructor(request: PersonalNotificationRequest, statistic: PersonalNotificationStatistic):
            this(request.notificationName, request.args, statistic.userId, request.email, statistic.types)

    fun containsType(type: NotificationDeliveryType) = types.contains(type)

}
