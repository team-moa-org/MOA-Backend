package moa.moabackend.domain.auth.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import moa.moabackend.domain.auth.presentation.dto.request.AdminSignupRequest
import moa.moabackend.domain.auth.presentation.dto.request.LoginRequest
import moa.moabackend.domain.auth.presentation.dto.request.ReissueRequest
import moa.moabackend.domain.auth.presentation.dto.request.SignupRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.auth.service.AdminSignupService
import moa.moabackend.domain.auth.service.LoginService
import moa.moabackend.domain.auth.service.ReissueService
import moa.moabackend.domain.auth.service.SignupService

@RestController
@RequestMapping("/auth")
@Tag(name = "인증 API", description = "회원가입, 로그인, 토큰 재발급을 담당합니다.")
class AuthController(
    private val signupService: SignupService,
    private val adminSignupService: AdminSignupService,
    private val loginService: LoginService,
    private val reissueService: ReissueService
) {

    @Operation(summary = "일반 회원가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody @Valid request: SignupRequest): TokenResponse {
        return signupService.execute(request)
    }

    @Operation(summary = "관리자(판매자) 회원가입")
    @PostMapping("/signup/admin")
    @ResponseStatus(HttpStatus.CREATED)
    fun adminSignup(@RequestBody @Valid request: AdminSignupRequest): TokenResponse {
        return adminSignupService.execute(request)
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): TokenResponse {
        return loginService.execute(request)
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    fun reissue(@RequestBody @Valid request: ReissueRequest): TokenResponse {
        return reissueService.execute(request)
    }
}
