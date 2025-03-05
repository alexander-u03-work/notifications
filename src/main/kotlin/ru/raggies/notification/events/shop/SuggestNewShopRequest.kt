package ru.raggies.notification.events.shop

data class SuggestNewShopRequest(
    val username: String,
    val shopName: String,
    val phoneNumber: String,
    val suggestions: String?,
)