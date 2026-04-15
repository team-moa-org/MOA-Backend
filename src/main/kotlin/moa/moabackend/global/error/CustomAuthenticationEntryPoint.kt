package moa.moabackend.global.error

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.JwtAuthenticationException

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin") ?: "")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS")
        response.setHeader("Access-Control-Allow-Headers", "*")
        response.setHeader("Access-Control-Max-Age", "3600")

        val errorCode = if (authException is JwtAuthenticationException) {
            authException.errorCode
        } else {
            (request.getAttribute("exception") as? ErrorCode) ?: ErrorCode.UNAUTHORIZED
        }

        response.status = errorCode.status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val errorResponse = ErrorResponse.Companion.of(
            errorCode,
            errorCode.message
        )

        objectMapper.writeValue(response.writer, errorResponse)
    }
}
