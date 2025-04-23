package ru.raggies.notification.events.service

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.DefaultJobParamsExtractor

@Component
class SuggestionImprovementEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = SuggestionImprovementEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> =
        params.getParameter("adminIds")?.value as List<Long>

    override fun getMessageArgs(params: JobParameters): Map<String, Any> {
        val args = mutableMapOf(
            "username" to params.getString("username")!!,
            "phoneNumber" to params.getString("phoneNumber")!!,
            "clientSatisfaction" to params.getString("clientSatisfaction")!!,
            "interfaceComfort" to params.getString("interfaceComfort")!!,
            "serviceWorkComfort" to params.getString("serviceWorkComfort")!!,
            "suggestion" to params.getString("suggestion")!!
        )

        return args
    }
}