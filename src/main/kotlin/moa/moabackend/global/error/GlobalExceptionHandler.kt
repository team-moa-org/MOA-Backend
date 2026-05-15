package moa.moabackend.global.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MoaException::class) // MoaException 발생 시 이 메서드 자동 실행
    fun handleMoaException(e: MoaException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        val response = ErrorResponse.of(errorCode, errorCode.message)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class) // Valid 예외 발생 시 실행
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.BAD_REQUEST
        val message = e.bindingResult.fieldError?.defaultMessage ?: errorCode.message
        val response = ErrorResponse.of(errorCode, message)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class) // JSON 파싱 에러 등
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.BAD_REQUEST
        val response = ErrorResponse.of(errorCode, "잘못된 JSON 요청입니다.")
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(Exception::class) // 예상치 못한 에러
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ErrorResponse.of(errorCode, e.message ?: "Unknown error")
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
