package ru.raggies.notification.events.product

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.DefaultJobParamsExtractor

@Component
class ProductModerationEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = ProductModerationEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> =
        params.getParameter("userIds")?.value as List<Long>

    override fun getMessageArgs(params: JobParameters): Map<String, Any> {
        val args = mutableMapOf(
            "productId" to params.getLong("productId")!!,
            "productTitle" to params.getString("productTitle")!!,
            "productUrl" to params.getString("productUrl")!!,
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