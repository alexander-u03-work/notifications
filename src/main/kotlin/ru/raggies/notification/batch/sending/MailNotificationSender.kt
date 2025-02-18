package ru.raggies.notification.batch.sending

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import ru.raggies.notification.batch.NotificationBatchData
import ru.raggies.notification.model.NotificationDeliveryType

@Component
class MailNotificationSender(
    private val mailSender: JavaMailSender,
): NotificationSender {

    companion object {
        private const val NO_REPLY_EMAIL = "noreply@raggies.ru"
    }

    override fun getType(): NotificationDeliveryType = NotificationDeliveryType.Email

    override fun sendNotification(data: NotificationBatchData, message: String) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage)
        helper.setFrom(NO_REPLY_EMAIL)
        helper.setTo(data.email)
        helper.setText(message, true)
        mailSender.send(mimeMessage)
    }

}