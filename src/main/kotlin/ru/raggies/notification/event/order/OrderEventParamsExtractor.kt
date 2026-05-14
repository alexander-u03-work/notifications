package ru.raggies.notification.event.order

import org.springframework.batch.core.JobParameters
import org.springframework.stereotype.Component
import ru.raggies.notification.event.DefaultJobParamsExtractor

@Component
class OrderEventParamsExtractor: DefaultJobParamsExtractor() {

    override fun getType(): String = OrderEvent::class.simpleName!!

    override fun getUserSubjects(params: JobParameters): List<String>
        = listOf(params.getString("userSubject")!!)

    override fun getMessageArgs(params: JobParameters): Map<String, Any> =
        mapOf(
            "orderId" to params.getLong("orderId")!!,
            "deliveryDate" to params.getParameter("deliveryDate")?.value!!,
            "payment" to params.getString("payment")!!,
        )

}