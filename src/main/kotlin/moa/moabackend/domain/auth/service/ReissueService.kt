package moa.moabackend.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import moa.moabackend.domain.auth.domain.repository.RefreshTokenRepository
import moa.moabackend.domain.auth.presentation.dto.request.ReissueRequest
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.global.security.jwt.JwtTokenProvider

@Service
class ReissueService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userFacade: UserFacade
) {

    @Transactional
    fun execute(request: ReissueRequest): TokenResponse {
        val userId = jwtTokenProvider.validateRefreshToken(request.refreshToken)
        val user = userFacade.findUserByTokenUserId(userId)

        refreshTokenRepository.deleteById(request.refreshToken)

        return jwtTokenProvider.generateTokens(userId, user.role.name)
    }
}
