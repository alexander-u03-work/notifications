package ru.raggies.notification.event

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Aspect
@Component
class EventHandlerLoggingAspect {

    val log = LoggerFactory.getLogger(EventHandlerLoggingAspect::class.java)!!

    @Around("execution(* ru.raggies.notification.events.EventHandler.handleEvent(..))")
    fun logHandlingEvent(joinPoint: ProceedingJoinPoint) {
        joinPoint.proceed()

        val methodArgs = joinPoint.args
        if (methodArgs.first() is Message<*>) {
            val message = methodArgs.first() as Message<*>

            val routingKey = message.headers[AmqpHeaders.RECEIVED_ROUTING_KEY]
            val queueName = message.headers[AmqpHeaders.CONSUMER_QUEUE]
            log.info("Handling event ${message.payload.javaClass} from queue: $queueName with routing key: $routingKey")
        }
    }

}