package ru.raggies.notification.notifications

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.NotificationEntity

interface NotificationRepository: JpaRepository<NotificationEntity, Long> {
}