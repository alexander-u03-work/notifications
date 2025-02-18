package ru.raggies.notification.events

import org.springframework.batch.core.JobParameters

interface NotificationEventJobParamsExtractor {

    fun getType(): String

    fun getEventType(params: JobParameters): String

    fun getNotificationName(params: JobParameters): String =
        params.getParameter("notificationName")
            ?.value as String

    fun getUserIds(params: JobParameters): List<Long>

    fun getMessageArgs(params: JobParameters): Map<String, Any>

}