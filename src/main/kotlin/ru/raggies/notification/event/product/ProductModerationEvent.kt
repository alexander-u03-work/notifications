package ru.raggies.notification.event.product

import ru.raggies.notification.event.NotificationEvent

class ProductModerationEvent(
    source: Product,
    operation: String,
    val productUrl: String,
    val shopOwners: List<String>,
    val issueType: String?,
    val comment: String?,
): NotificationEvent<Product>(source, operation)