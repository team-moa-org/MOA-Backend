package moa.moabackend.global.security

import moa.moabackend.domain.user.domain.exception.UserNotFoundException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw UserNotFoundException
        return authentication.name.toLongOrNull() ?: throw UserNotFoundException
    }
}
