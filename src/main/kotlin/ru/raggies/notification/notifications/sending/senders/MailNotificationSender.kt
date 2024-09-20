package ru.raggies.notification.notifications.sending.senders

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import ru.raggies.notification.model.NotificationDeliveryType
import ru.raggies.notification.notifications.users.UserNotificationSubscription

@Component
class MailNotificationSender(
    private val mailSender: JavaMailSender,
): NotificationSender {

    companion object {
        private const val NO_REPLY_EMAIL = "noreply@raggies.ru"
    }

    override fun canSend(type: NotificationDeliveryType) =
        type == NotificationDeliveryType.Email

    override fun sendNotification(info: UserNotificationSubscription, htmlBody: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.setFrom(NO_REPLY_EMAIL)
        helper.setTo(info.email)
        helper.setText(htmlBody, true)
        mailSender.send(message)
    }

}