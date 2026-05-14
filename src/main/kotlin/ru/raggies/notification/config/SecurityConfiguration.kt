package ru.raggies.notification.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration

@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@Configuration
@EnableConfigurationProperties(SecurityProperties::class)
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    private val securityProperties: SecurityProperties
) {

    companion object {
        val log = LoggerFactory.getLogger(SecurityConfiguration::class.java)!!
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.configurationSource { _ -> corsConfigurationSource() } }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/actuator/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { resource -> resource.jwt { jwtConfigurer ->
                jwtConfigurer.jwtAuthenticationConverter(authenticationConverter())
            } }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { handler -> handler.authenticationEntryPoint(authenticationEntryPoint()) }
            .build()

    @Bean
    fun corsConfigurationSource(): CorsConfiguration {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = securityProperties.allowedOrigins
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        configuration.maxAge = 3600

        return configuration
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint = AuthenticationEntryPoint { _, response, ex ->
        log.error("Attempt to get an access to private resource", ex)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
    }

    @Bean
    fun authenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConvertor = JwtGrantedAuthoritiesConverter()
        grantedAuthoritiesConvertor.setAuthorityPrefix("")
        grantedAuthoritiesConvertor.setAuthoritiesClaimName("authorities")

        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConvertor)

        return converter
    }

}