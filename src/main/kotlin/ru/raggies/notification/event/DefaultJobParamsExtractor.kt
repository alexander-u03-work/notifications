package ru.raggies.notification.event

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component

@Component
class DefaultJobParamsExtractor: NotificationEventJobParamsExtractor {

    companion object {
        const val EVENT_TYPE = "eventType"
    }

    override fun getEventType(params: JobParameters): String =
        params.getString(EVENT_TYPE)!!

    override fun getType(): String = NotificationEvent::class.simpleName!!

    override fun getUserSubjects(params: JobParameters): List<String> = throw UnsupportedOperationException()

    override fun getMessageArgs(params: JobParameters): Map<String, Any> = throw UnsupportedOperationException()
}