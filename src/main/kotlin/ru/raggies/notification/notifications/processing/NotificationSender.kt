package ru.raggies.notification.notifications.processing

import freemarker.template.Template
import org.slf4j.LoggerFactory
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import ru.raggies.notification.create
import ru.raggies.notification.enums.NotificationDeliveryType
import ru.raggies.notification.exceptions.ApplicationException
import ru.raggies.notification.notifications.NotificationListener
import ru.raggies.notification.notifications.UserNotificationInfo
import ru.raggies.notification.subscribtions.NotificationInfo
import ru.raggies.notification.tables.daos.UserNotificationDao
import ru.raggies.notification.tables.pojos.UserNotification
import ru.raggies.notification.templates.TemplateManager
import java.time.Instant

@Component
class NotificationSender(
    private val templateManager: TemplateManager,
    private val freeMarkerConfigurer: FreeMarkerConfigurer,
    private val mailSender: JavaMailSender,
    private val userNotificationDao: UserNotificationDao
): ItemWriter<UserNotificationInfo> {

    companion object {
        private val log = LoggerFactory.getLogger(NotificationListener::class.java)!!

        private const val NO_REPLY_EMAIL = "noreply@raggies.ru"
    }

    override fun write(chunk: Chunk<out UserNotificationInfo>) {
        chunk.forEach { sendNotification(it) }
    }

    fun createUserNotification(notificationInfo: UserNotificationInfo, htmlBody: String): NotificationInfo {
        val userNotification = userNotificationDao.create(UserNotification(
            userId = notificationInfo.userId,
            notificationName = notificationInfo.notificationName,
            message = htmlBody,
            sentAt = Instant.now()
        ))
        return NotificationInfo(userNotification)
    }

    fun sendNotification(notificationInfo: UserNotificationInfo) {
        val template = templateManager.getTemplateByName(notificationInfo.notificationName) ?:
            throw ApplicationException("Template with name ${notificationInfo.notificationName} not found")

        val htmlBody = processEmail(notificationInfo.notificationName, template.content!!, notificationInfo.args)
        if (notificationInfo.containsType(NotificationDeliveryType.Email)) {
            sendHTMLMessage(notificationInfo.email, htmlBody)

            log.info("Sending notification ${notificationInfo.notificationName}")
        }
        if (notificationInfo.containsType(NotificationDeliveryType.Push)) {
            createUserNotification(notificationInfo, htmlBody)
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