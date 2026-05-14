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
    fun createUserNotification(userSubject: String, notificationName: String, message: String) {
        userNotificationRepository.save(UserNotificationEntity(
            userSubject = userSubject,
            notificationName = notificationName,
            message = message,
            sentAt = Instant.now(),
        ))
    }

    fun getUserNotifications(userSubject: String): List<UserNotification> =
        userNotificationRepository.findByUserSubjectOrderBySentAtDesc(userSubject)
            .map { it.toDTO() }

}