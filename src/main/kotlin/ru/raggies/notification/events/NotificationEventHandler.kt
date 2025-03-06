package ru.raggies.notification.events

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.messaging.Message
import ru.raggies.notification.events.DefaultJobParamsExtractor.Companion.EVENT_TYPE
import java.time.LocalDateTime

abstract class NotificationEventHandler<E: NotificationEvent<*>>(
    private val notificationJob: Job,
    private val jobLauncher: JobLauncher,
): EventHandler<E> {

    override fun handleEvent(message: Message<E>) {
        val params = getJobParameters(message.payload)
            .addLocalDateTime("now", LocalDateTime.now())
            .addString(EVENT_TYPE, message.payload::class.simpleName!!)
            .toJobParameters()

        jobLauncher.run(notificationJob, params)
    }

    protected abstract fun getJobParameters(event: E): JobParametersBuilder

}