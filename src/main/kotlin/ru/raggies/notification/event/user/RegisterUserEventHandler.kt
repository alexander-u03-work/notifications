package ru.raggies.notification.event.user

import event.EventHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import ru.raggies.notification.subscriptions.SubscriptionManager

@Component
class RegisterUserEventHandler(
    private val subscriptionManager: SubscriptionManager
): EventHandler<UserRegisterEvent> {

    @RabbitListener(queues = [$$"${app.rabbitmq.queues.users-registration.queue-name}"])
    override fun handleEvent(event: Message<UserRegisterEvent>) {
        subscriptionManager.subscribeUser(event.payload.source)
    }
}