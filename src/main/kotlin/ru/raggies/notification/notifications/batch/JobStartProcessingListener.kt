package ru.raggies.notification.notifications.batch

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component
import java.util.*

@Component
class JobStartProcessingListener(
    private val jobLauncher: JobLauncher,
    private val notificationJob: Job
) {

    @RabbitListener(queues = ["job-processing"])
    fun startJobProcessing(request: JobStartRequest) {
        val jobParameters = JobParametersBuilder()
            .addString("uniqueness", UUID.randomUUID().toString())
            .toJobParameters()
        jobLauncher.run(notificationJob, jobParameters)
    }

}