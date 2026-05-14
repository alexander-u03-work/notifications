package ru.raggies.notification.event.user

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.event.DefaultJobParamsExtractor

@Component
class UserModerationEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = UserModerationEvent::class.simpleName!!

    override fun getUserSubjects(params: JobParameters): List<String>
        = listOf(params.getString("subject")!!)

    override fun getMessageArgs(params: JobParameters): Map<String, Any> {
        val args = mutableMapOf<String, Any>(
            "userId" to params.getLong("userId")!!
        )

        params.getString("issueType")?.also {
            args["issueType"] = it
        }
        params.getString("comment")?.also {
            args["comment"] = it
        }

        return args
    }

}