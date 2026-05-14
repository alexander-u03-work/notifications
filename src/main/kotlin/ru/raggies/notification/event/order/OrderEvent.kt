package ru.raggies.notification.event.order

import ru.raggies.notification.event.NotificationEvent

class OrderEvent(
    source: Order,
    operation: String
): NotificationEvent<Order>(source, operation)