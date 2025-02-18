package ru.raggies.notification.events.order

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.events.DefaultJobParamsExtractor

@Component
class OrderEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = OrderEvent::class.simpleName!!

    override fun getUserIds(params: JobParameters): List<Long> =
        listOf(params.getLong("userId")!!)

    override fun getMessageArgs(params: JobParameters): Map<String, Any> =
        mapOf(
            "orderId" to params.getLong("orderId")!!,
            "deliveryDate" to params.getParameter("deliveryDate")?.value!!,
            "payment" to params.getString("payment")!!,
        )

}