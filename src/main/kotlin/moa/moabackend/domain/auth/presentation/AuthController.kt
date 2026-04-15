package moa.moabackend.domain.auth.presentation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import moa.moabackend.domain.auth.presentation.dto.request.LoginRequest
import moa.moabackend.domain.auth.presentation.dto.request.ReissueRequest
import moa.moabackend.domain.auth.presentation.dto.request.SignupRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.auth.service.LoginService
import moa.moabackend.domain.auth.service.ReissueService
import moa.moabackend.domain.auth.service.SignupService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val signupService: SignupService,
    private val loginService: LoginService,
    private val reissueService: ReissueService
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody @Valid request: SignupRequest) {
        signupService.execute(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): TokenResponse {
        return loginService.execute(request)
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody @Valid request: ReissueRequest): TokenResponse {
        return reissueService.execute(request)
    }
}
