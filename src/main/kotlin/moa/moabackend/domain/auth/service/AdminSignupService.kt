package moa.moabackend.domain.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.presentation.dto.request.AdminSignupRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.user.domain.Role
import moa.moabackend.domain.user.domain.User
import moa.moabackend.domain.user.domain.exception.UserAlreadyExistsException
import moa.moabackend.domain.user.domain.repository.UserRepository
import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException
import moa.moabackend.global.security.jwt.JwtTokenProvider

@Service
class AdminSignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    @Value("\${admin.key}")
    private val adminKey: String
) {

    @Transactional
    fun execute(request: AdminSignupRequest): TokenResponse {
        if (request.adminKey != adminKey) {
            throw MoaException(ErrorCode.INVALID_ADMIN_KEY)
        }

        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException
        }

        val user = User(
            email = request.email,
            name = request.name,
            password = passwordEncoder.encode(request.password),
            role = Role.ADMIN,
            farmName = request.farmName
        )

        val savedUser = userRepository.save(user)
        return jwtTokenProvider.generateTokens(savedUser.id, savedUser.role.name)
    }
}
