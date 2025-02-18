package ru.raggies.notification.events.order

import ru.raggies.notification.events.NotificationEvent

class OrderEvent(
    source: Order,
    operation: String
): NotificationEvent<Order>(source, operation)