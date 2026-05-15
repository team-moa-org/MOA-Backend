package moa.moabackend.domain.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.presentation.dto.request.SignupRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.user.domain.Role
import moa.moabackend.domain.user.domain.User
import moa.moabackend.domain.user.domain.exception.UserAlreadyExistsException
import moa.moabackend.domain.user.domain.repository.UserRepository
import moa.moabackend.global.security.jwt.JwtTokenProvider

@Service
class SignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun execute(request: SignupRequest): TokenResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException
        }

        val user = User(
            email = request.email,
            name = request.name,
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )

        val savedUser = userRepository.save(user)
        return jwtTokenProvider.generateTokens(savedUser.id, savedUser.role.name)
    }
}
