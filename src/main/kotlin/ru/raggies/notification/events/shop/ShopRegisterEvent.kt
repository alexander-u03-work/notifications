package ru.raggies.notification.events.shop

import ru.raggies.notification.events.NotificationEvent

class ShopRegisterEvent(
    source: SuggestNewShopRequest,
    operation: String,
    val adminIds: List<Long>,
) : NotificationEvent<SuggestNewShopRequest>(source, operation)