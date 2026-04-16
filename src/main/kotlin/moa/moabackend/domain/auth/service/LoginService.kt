package moa.moabackend.domain.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.presentation.dto.request.LoginRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.user.domain.exception.PasswordMismatchException
import moa.moabackend.domain.user.domain.exception.UserNotFoundException
import moa.moabackend.domain.user.domain.repository.UserRepository
import moa.moabackend.global.security.jwt.JwtTokenProvider

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun execute(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw PasswordMismatchException
        }

        return jwtTokenProvider.generateTokens(user.id, user.role.name)
    }
}
