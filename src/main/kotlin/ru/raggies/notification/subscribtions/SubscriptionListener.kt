package ru.raggies.notification.subscribtions

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class SubscriptionListener(
    private val subscriptionManager: UserSubscriptionManager
) {

    @RabbitListener(queues = ["subscribe-on-register"])
    fun subscribeOnRegisterListener(request: SubscribeOnRegisterRequest) {
        subscriptionManager.subscribeUserForNotifications(request)
    }

}