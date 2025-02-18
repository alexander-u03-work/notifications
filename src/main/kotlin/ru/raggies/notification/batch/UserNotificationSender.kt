package ru.raggies.notification.batch

import freemarker.template.Template
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import ru.raggies.notification.batch.sending.NotificationSender
import ru.raggies.notification.templates.TemplateManager

@Component
class UserNotificationSender(
    private val templateManager: TemplateManager,
    private val senders: List<NotificationSender>,
    private val freeMarkerConfigurer: FreeMarkerConfigurer,
): ItemWriter<NotificationBatchData> {

    private val sendersByType by lazy {
        senders.associateBy { it.getType() }
    }

    override fun write(chunk: Chunk<out NotificationBatchData>) {
        val templatesByNotificationName = chunk.items
            .distinctBy { it.notificationName }
            .associateBy( {it.notificationName} ) {
                templateManager.getTemplateByName(it.notificationName)
            }

        chunk.forEach {
            val template = templatesByNotificationName[it.notificationName]!!
            sendNotification(it, template)
        }
    }

    private fun sendNotification(data: NotificationBatchData, template: ru.raggies.notification.templates.Template) {
        val message = buildMessage(data.notificationName, template.content, data.messageArgs)

        sendersByType[data.notificationType]
            ?.sendNotification(data, message)
    }

    private fun buildMessage(templateName: String, templateContent: String, args: Map<String, Any>): String {
        val freeMarkerTemplate = Template(
            templateName,
            templateContent,
            freeMarkerConfigurer.configuration
        )
        return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, args)
    }
}