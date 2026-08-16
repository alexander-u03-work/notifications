package ru.raggies.notification.notifications

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import exception.ApplicationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.raggies.notification.model.NotificationEntity
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Service
class NotificationManager(
    private val notificationRepository: NotificationRepository,
    private val notificationTypeRepository: NotificationTypeRepository
) {

    private val notificationsCache: LoadingCache<Long, List<Notification>> = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build(CacheLoader.from(::createNotificationsCache))

    private val version: AtomicLong = AtomicLong()


    @Transactional
    fun createNotification(request: CreateNotificationRequest): Notification {
        val notificationType = notificationTypeRepository.findById(request.typeId)
            .orElseThrow { ApplicationException("Unknown notification type: ${request.typeId}") }

        val savedEntity = notificationRepository.save(NotificationEntity(
            name = request.name,
            type = notificationType,
            isRequired = request.isRequired
        ))
        version.getAndIncrement()

        return savedEntity.toDTO()
    }

    fun getNotifications(): List<Notification> =
        notificationsCache.get(version.get())

    fun getNotificationTypes(): List<NotificationType> =
        notificationTypeRepository.findAll()
            .map { it.toDTO() }

    private fun createNotificationsCache(): List<Notification> {
        version.getAndIncrement()

        return notificationRepository.findAll()
            .map { it.toDTO() }
    }

}