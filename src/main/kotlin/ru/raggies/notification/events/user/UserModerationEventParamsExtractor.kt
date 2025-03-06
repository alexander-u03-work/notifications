package ru.raggies.notification.events.user

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.DefaultJobParamsExtractor

@Component
class UserModerationEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = UserModerationEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> =
        listOf(params.getLong("userId")!!)

    override fun getMessageArgs(params: JobParameters): Map<String, Any> {
        val args = mutableMapOf<String, Any>(
            "userId" to params.getLong("userId")!!
        )

        params.getString("issueType")?.also {
            args.put("issueType", it)
        }
        params.getString("comment")?.also {
            args.put("comment", it)
        }

        return args
    }

}