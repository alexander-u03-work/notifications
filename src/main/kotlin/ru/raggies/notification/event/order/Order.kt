package ru.raggies.notification.event.order

import java.time.Instant

data class Order(
    val id: Long,
    val userId: Long,
    val deliveryDate: Instant,
    val payment: String,
)