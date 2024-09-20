package ru.raggies.notification.model

import jakarta.persistence.*
import ru.raggies.notification.templates.Template

@Entity
@Table(name = "template", schema = "notification_schema")
class TemplateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String? = null,

    @Column(nullable = false)
    val content: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: TemplateStatus? = null
) {
    fun toDTO() = Template(id!!, name!!, content!!, status!!)
}