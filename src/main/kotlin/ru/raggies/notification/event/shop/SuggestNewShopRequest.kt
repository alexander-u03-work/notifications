package ru.raggies.notification.event.shop

data class SuggestNewShopRequest(
    val username: String,
    val shopName: String,
    val phoneNumber: String,
    val suggestion: String?,
)