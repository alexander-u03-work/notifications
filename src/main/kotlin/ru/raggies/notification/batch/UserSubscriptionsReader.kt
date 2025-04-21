package ru.raggies.notification.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParameters
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader
import ru.raggies.notification.events.NotificationEventJobParamsExtractor
import ru.raggies.notification.subscriptions.SubscriptionManager
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

open class UserSubscriptionsReader(
    private val jobParameters: JobParameters,
    private val subscriptionManager: SubscriptionManager,
    private val defaultJobParamsExtractor: NotificationEventJobParamsExtractor,
    private val jobParamsExtractors: List<NotificationEventJobParamsExtractor>,
): AbstractItemCountingItemStreamItemReader<NotificationBatchData>() {

    companion object {
        val log = LoggerFactory.getLogger(UserSubscriptionsReader::class.java)!!
    }

    private lateinit var iterator: Iterator<NotificationBatchData>
    private val lock: Lock = ReentrantLock()

    private val extractorsByType by lazy {
        jobParamsExtractors.associateBy { it.getType() }
    }

    override fun doRead(): NotificationBatchData? {
        lock.lock()
        return try {
            if (iterator.hasNext()) {
                val subscription = iterator.next()
                log.debug("Reading user's with id {} and email {} subscription", subscription.userId, subscription.email)

                return subscription
            } else {
                null
            }
        } finally {
            lock.unlock()
        }
    }


    override fun doOpen() {
        val extractor = getExtractor()

        val notificationName = extractor.getNotificationName(jobParameters)
        val userIds = extractor.getUserIds(jobParameters)

        log.debug("Starting user's subscriptions batch {}", userIds)
        val subscriptions = subscriptionManager.getUserSubscriptions(notificationName, userIds)
            .map { data -> NotificationBatchData(data, extractor.getMessageArgs(jobParameters)) }

        log.debug("Users with ids {} subscriptions: {}", userIds, subscriptions)

        this.iterator = subscriptions.iterator()
    }

    override fun doClose() {
    }

    private fun getExtractor(): NotificationEventJobParamsExtractor {
        val eventType = defaultJobParamsExtractor.getEventType(jobParameters)

        return extractorsByType[eventType]
            ?: defaultJobParamsExtractor
    }

}