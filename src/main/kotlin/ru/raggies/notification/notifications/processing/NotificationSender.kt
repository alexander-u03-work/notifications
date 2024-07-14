package ru.raggies.notification.notifications.processing

import freemarker.template.Template
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.annotation.BeforeWrite
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.notifications.NotificationRequest
import ru.raggies.notification.notifications.UserNotificationManager
import ru.raggies.notification.templates.TemplateManager
import java.util.concurrent.atomic.AtomicReference

@Component
class NotificationSender(
    private val userNotificationManager: UserNotificationManager,
    private val templateManager: TemplateManager,
    private val freeMarkerConfigurer: FreeMarkerConfigurer,
    private val mailSender: JavaMailSender,
): ItemWriter<UserNotificationStatistic> {

    companion object {
        private val log = LoggerFactory.getLogger(NotificationListener::class.java)!!

        private const val NO_REPLY_EMAIL = "noreply@raggies.ru"
    }

    override fun write(chunk: Chunk<out UserNotificationStatistic>) {
        chunk.forEach { sendNotification(it) }
    }

    fun sendNotification(statistic: UserNotificationStatistic) {
        if (statistic.types.contains(NotificationDeliveryType.Email)) {
            val template = templateManager.getTemplateByName(statistic.notificationName) ?:
                throw ApplicationException("Template with name ${statistic.notificationName} not found")

            processEmail(statistic.notificationName, template.content!!, statistic.args)
                .also { htmlBody -> sendHTMLMessage(statistic.email, htmlBody) }

            log.info("Sending notification ${statistic.notificationName}")
        }
        if (statistic.types.contains(NotificationDeliveryType.Push)) {
            userNotificationManager.createUserNotification(statistic)
        }
    }

    private fun processEmail(templateName: String, templateContent: String, args: Map<String, String>): String {
        val freeMarkerTemplate = Template(
            templateName,
            templateContent,
            freeMarkerConfigurer.configuration
        )
        return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, args)
    }

    private fun sendHTMLMessage(to: String, htmlBody: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.setFrom(NO_REPLY_EMAIL)
        helper.setTo(to)
        helper.setText(htmlBody, true)
        mailSender.send(message)
    }
}