package ru.raggies.notification.events

import org.springframework.messaging.Message

interface EventHandler<E: NotificationEvent<*>> {

    fun handleEvent(message: Message<E>)

}