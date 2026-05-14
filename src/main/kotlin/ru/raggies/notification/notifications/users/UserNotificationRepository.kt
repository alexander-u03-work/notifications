package ru.raggies.notification.notifications.users

import org.springframework.data.jpa.repository.JpaRepository
import ru.raggies.notification.model.UserNotificationEntity
import java.util.*

interface UserNotificationRepository: JpaRepository<UserNotificationEntity, Long> {

    fun findByUserSubjectOrderBySentAtDesc(subject: String): List<UserNotificationEntity>

}