package ru.raggies.notification.notifications.batch

import java.time.Instant

class JobStartRequest(
    val message: Int,
    val sentAt: Instant
)
