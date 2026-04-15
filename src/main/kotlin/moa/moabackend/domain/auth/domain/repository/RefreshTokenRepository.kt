package moa.moabackend.domain.auth.domain.repository

import org.springframework.data.repository.CrudRepository
import moa.moabackend.domain.auth.domain.RefreshToken

interface RefreshTokenRepository : CrudRepository<RefreshToken, String>
