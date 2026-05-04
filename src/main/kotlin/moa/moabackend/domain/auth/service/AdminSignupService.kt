package moa.moabackend.domain.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.presentation.dto.request.AdminSignupRequest
import moa.moabackend.domain.user.domain.Role
import moa.moabackend.domain.user.domain.User
import moa.moabackend.domain.user.domain.exception.UserAlreadyExistsException
import moa.moabackend.domain.user.domain.repository.UserRepository
import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

@Service
class AdminSignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun execute(request: AdminSignupRequest) {
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

        userRepository.save(user)
    }
}
