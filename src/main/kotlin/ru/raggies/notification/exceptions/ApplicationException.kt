package ru.raggies.notification.exceptions

class ApplicationException(
    message: String?,
    throwable: Throwable?
): RuntimeException(message, throwable) {

    constructor(message: String?): this(message, null)
}