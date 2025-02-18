package ru.raggies.notification.events

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class NotificationEvent<T>(
    val source: T,
    val operation: String
) {

    @JsonIgnore
    fun getNotificationName(): String {
        val operation = operation.lowercase().replaceFirstChar { it.uppercase() }

        return "${source!!::class.simpleName}${operation}Notification"
    }

}