package ru.raggies.notification.event.shop

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.event.DefaultJobParamsExtractor

@Component
class ShopRegisterEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = ShopRegisterEvent::class.simpleName!!

    override fun getUserSubjects(params: JobParameters): List<String>
        = params.getParameter("adminSubjects")?.value as List<String>

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