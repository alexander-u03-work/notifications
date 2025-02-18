package ru.raggies.notification.events

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.NotificationEventHandler.Companion.EVENT_TYPE

@Component
class DefaultJobParamsExtractor: NotificationEventJobParamsExtractor {

    override fun getEventType(params: JobParameters): String =
        params.getString(EVENT_TYPE)!!

    override fun getType(): String = NotificationEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> = throw UnsupportedOperationException()

    override fun getMessageArgs(params: JobParameters): Map<String, Any> = throw UnsupportedOperationException()
}