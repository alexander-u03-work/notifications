package ru.raggies.notification.batch.sending

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import ru.raggies.notification.batch.NotificationBatchData
import ru.raggies.notification.config.MailProperties
import ru.raggies.notification.model.NotificationDeliveryType

@Component
class MailNotificationSender(
    private val mailSender: JavaMailSender,
    private val messageSource: MessageSource,
    private val properties: MailProperties
): NotificationSender {

    override fun getType(): NotificationDeliveryType = NotificationDeliveryType.Email

    override fun sendNotification(data: NotificationBatchData, message: String) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        helper.setFrom(properties.mail)
        helper.setTo(data.email)
        helper.setSubject(getSubject(data.notificationName))
        helper.setText(message, true)
        mailSender.send(mimeMessage)
    }

    private fun getSubject(notificationName: String): String =
        messageSource.getMessage(
            "notification.mail.subject.$notificationName",
            null,
            notificationName,
            LocaleContextHolder.getLocale()
        )!!

}