package ru.raggies.notification

import org.springframework.security.oauth2.jwt.Jwt

fun Jwt.getUserId(): Long = getClaim("user_id")