package moa.moabackend.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 엔티티를 찾을 수 없습니다"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 타입의 입력값"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
