package ru.raggies.notification.events.order

import java.time.Instant
import java.util.*

data class Order(
    val id: Long,
    val userId: Long,
    val deliveryDate: Instant,
    val payment: String,
)