package ru.raggies.notification.notifications.users

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.model.UserNotificationEntity
import java.time.Instant

@Service
class UserNotificationManager(
    private val userNotificationRepository: UserNotificationRepository,
) {

    @Transactional
    fun createUserNotification(userId: Long, notificationName: String, message: String) {
        userNotificationRepository.save(UserNotificationEntity(
            userId = userId,
            notificationName = notificationName,
            message = message,
            sentAt = Instant.now(),
        ))
    }

    fun getUserNotifications(userId: Long): List<UserNotification> =
        userNotificationRepository.findByUserIdOrderBySentAtDesc(userId)
            .map { it.toDTO() }

}