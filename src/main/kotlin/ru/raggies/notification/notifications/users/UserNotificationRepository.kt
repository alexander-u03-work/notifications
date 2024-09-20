package ru.raggies.notification.notifications.users

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.UserNotificationEntity

interface UserNotificationRepository: JpaRepository<UserNotificationEntity, Long> {

    fun findAllByOrderBySentAtDesc(): List<UserNotificationEntity>

}