package ru.raggies.notification.events.product

import ru.raggies.notification.events.NotificationEvent

class ProductModerationEvent(
    source: Product,
    operation: String,
    val productUrl: String,
    val shopOwners: List<Long>,
    val issueType: String?,
    val comment: String?,
): NotificationEvent<Product>(source, operation)