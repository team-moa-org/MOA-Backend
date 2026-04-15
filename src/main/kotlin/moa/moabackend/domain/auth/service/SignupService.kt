package moa.moabackend.domain.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.presentation.dto.request.SignupRequest
import moa.moabackend.domain.user.domain.Role
import moa.moabackend.domain.user.domain.User
import moa.moabackend.domain.user.domain.exception.UserAlreadyExistsException
import moa.moabackend.domain.user.domain.repository.UserRepository

@Service
class SignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun execute(request: SignupRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException
        }

        val user = User(
            email = request.email,
            name = request.name,
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )

        userRepository.save(user)
    }
}
