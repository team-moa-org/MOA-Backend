package moa.moabackend.domain.user.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import moa.moabackend.domain.user.domain.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
