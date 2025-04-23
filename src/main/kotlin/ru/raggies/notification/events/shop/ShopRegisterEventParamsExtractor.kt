package ru.raggies.notification.events.shop

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.DefaultJobParamsExtractor

@Component
class ShopRegisterEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = ShopRegisterEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> =
        params.getParameter("adminIds")?.value as List<Long>

    override fun getMessageArgs(params: JobParameters): Map<String, Any> {
        val args = mutableMapOf(
            "username" to params.getString("username")!!,
            "shopName" to params.getString("shopName")!!,
            "phoneNumber" to params.getString("phoneNumber")!!,
            "suggestion" to params.getString("suggestion")!!
        )

        return args
    }
}