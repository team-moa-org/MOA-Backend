package moa.moabackend.domain.user.service.facade

import moa.moabackend.domain.user.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import moa.moabackend.domain.user.domain.exception.UserNotFoundException
import moa.moabackend.global.security.exception.InvalidTokenException

@Component
class UserFacade(
    private val userRepository: UserRepository
) {

    fun findByUserId(userId: Long) =
        userRepository.findByIdOrNull(userId)
            ?.takeIf { !it.isDeleted() }
            ?: throw UserNotFoundException

    fun findUserByTokenUserId(userId: Long) =
        userRepository.findByIdOrNull(userId)
            ?.takeIf { !it.isDeleted() }
            ?: throw InvalidTokenException
}
