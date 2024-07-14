package ru.raggies.notification.templates

import org.springframework.stereotype.Service
import ru.raggies.notification.enums.TemplateStatus
import ru.raggies.notification.tables.Template.Companion.TEMPLATE
import ru.raggies.notification.tables.daos.TemplateDao
import ru.raggies.notification.tables.pojos.Template

@Service
class TemplateManager(
    private val templateDao: TemplateDao
) {

    fun getTemplates(): List<Template> = templateDao.ctx()
        .selectFrom(TEMPLATE)
        .where(TEMPLATE.STATUS.eq(TemplateStatus.Created))
        .fetchInto(Template::class.java)

    fun getTemplateByName(templateName: String): Template? = templateDao.ctx()
        .select(TEMPLATE)
        .from(TEMPLATE)
        .where(TEMPLATE.NAME.eq(templateName))
        .and(TEMPLATE.STATUS.eq(TemplateStatus.Created))
        .fetchOneInto(Template::class.java)

}