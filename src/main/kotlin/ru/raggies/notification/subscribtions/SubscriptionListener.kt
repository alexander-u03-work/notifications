package ru.raggies.notification.subscribtions

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class SubscriptionListener(
    private val subscriptionManager: SubscriptionManager
) {

    @RabbitListener(queues = ["subscribe-on-register"])
    fun subscribeOnRegisterListener(request: SubscribeRequest) {
        subscriptionManager.subscribeUser(request)
    }

}