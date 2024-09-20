package ru.raggies.notification.notifications.sending

import freemarker.template.Template
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import ru.raggies.notification.notifications.batch.NotificationProcessRequest
import ru.raggies.notification.notifications.sending.senders.NotificationSender
import ru.raggies.notification.notifications.users.UserNotificationSubscription
import ru.raggies.notification.templates.TemplateManager
import java.awt.SystemColor.info

@Component
class NotificationDeliveryProvider(
    private val templateManager: TemplateManager,
    private val freeMarkerConfigurer: FreeMarkerConfigurer,
    private val senders: List<NotificationSender>,
): ItemWriter<NotificationProcessRequest> {

    override fun write(chunk: Chunk<out NotificationProcessRequest>) {
        chunk.forEach { request -> sendNotification(request) }
    }

    private fun sendNotification(request: NotificationProcessRequest) {
        val template = templateManager.getTemplateByName(request.notification.notificationName())

        val message = buildMessage(request.notification.notificationName(), template.content, request.messageArgs)
        sendNotification(request.notification, message)
    }

    private fun sendNotification(notification: UserNotificationSubscription, message: String) {
        senders
            .filter { s -> s.canSend(notification.notification.typeName) }
            .forEach { s -> s.sendNotification(notification, message) }
    }

    private fun buildMessage(templateName: String, templateContent: String, args: Map<String, String>): String {
        val freeMarkerTemplate = Template(
            templateName,
            templateContent,
            freeMarkerConfigurer.configuration
        )
        return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, args)
    }

}