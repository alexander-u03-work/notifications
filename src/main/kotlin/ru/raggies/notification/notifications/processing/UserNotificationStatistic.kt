package ru.raggies.notification.notifications.processing

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.raggies.notification.enums.NotificationDeliveryType

data class UserNotificationStatistic(
    val notificationName: String,
    val args: Map<String, String>,
    val email: String,
    val userId: Long,
    val types: List<NotificationDeliveryType>
) {

    @JsonIgnore
    fun message() = args.values.toString()

}
