package ru.raggies.notification.event

import org.springframework.batch.core.JobParameters

interface NotificationEventJobParamsExtractor {

    fun getType(): String

    fun getEventType(params: JobParameters): String

    fun getNotificationName(params: JobParameters): String =
        params.getParameter("notificationName")
            ?.value as String

    fun getUserSubjects(params: JobParameters): List<String>

    fun getMessageArgs(params: JobParameters): Map<String, Any>

}