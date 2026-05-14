package ru.raggies.notification.event

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

abstract class NotificationEvent<T>(
    val source: T,
    val operation: String
): Serializable {

    @JsonIgnore
    fun getNotificationName(): String {
        val operation = operation.lowercase().replaceFirstChar { it.uppercase() }

        return "${source!!::class.simpleName}${operation}Notification"
    }

}