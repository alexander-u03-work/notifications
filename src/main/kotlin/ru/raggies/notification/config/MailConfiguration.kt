package ru.raggies.notification.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
@EnableConfigurationProperties(MailProperties::class)
class MailConfiguration {

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("notification")
        messageSource.setDefaultEncoding("UTF-8")

        return messageSource
    }

}