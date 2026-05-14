package ru.raggies.notification.event.shop

import ru.raggies.notification.event.NotificationEvent

class ShopRegisterEvent(
    source: SuggestNewShopRequest,
    operation: String,
    val adminSubjects: List<String>,
) : NotificationEvent<SuggestNewShopRequest>(source, operation)