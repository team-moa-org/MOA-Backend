package moa.moabackend.global.error.exception

import org.springframework.security.core.AuthenticationException

/**
 * 인증 실패 시 Spring Security가 AuthenticationException를 인식하여
 * AuthenticationEntryPoint로 예외를 전달하고 처리함
 * JWT 인증 예외 등은 WeeShException이 아닌
 * AuthenticationException을 상속한 커스텀 예외로 감싸서 던져야함
 */
class JwtAuthenticationException(
    val errorCode: ErrorCode,
    cause: Throwable? = null // 원본 예외를 저장하기 위해
) : AuthenticationException(errorCode.message)
