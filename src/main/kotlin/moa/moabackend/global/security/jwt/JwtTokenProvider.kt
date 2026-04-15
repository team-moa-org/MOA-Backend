package moa.moabackend.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import moa.moabackend.domain.auth.domain.RefreshToken
import moa.moabackend.domain.auth.domain.repository.RefreshTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import moa.moabackend.domain.auth.presentation.dto.response.TokenResponse
import moa.moabackend.global.security.exception.ExpiredTokenException
import moa.moabackend.global.security.exception.InvalidTokenException
import java.time.LocalDateTime
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    companion object {
        private const val TOKEN_TYPE_ACCESS = "access"
        private const val TOKEN_TYPE_REFRESH = "refresh"
    }

    fun generateTokens(userId: Long) = TokenResponse(
        accessToken = generateAccessToken(userId),
        accessExp = LocalDateTime.now().plusSeconds(jwtProperties.accessExp),
        refreshToken = generateRefreshToken(userId),
        refreshExp = LocalDateTime.now().plusSeconds(jwtProperties.refreshExp)
    )

    private fun generateAccessToken(userId: Long) =
        Jwts.builder()
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setHeaderParam(Header.TYPE, TOKEN_TYPE_ACCESS)
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessExp * 1000L))
            .compact()

    private fun generateRefreshToken(userId: Long): String {
        val token = Jwts.builder()
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setHeaderParam(Header.TYPE, TOKEN_TYPE_REFRESH)
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.refreshExp * 1000L))
            .compact()

        val refreshToken = RefreshToken(
            token = token,
            userId = userId,
            ttl = jwtProperties.refreshExp
        )
        refreshTokenRepository.save(refreshToken)

        return token
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)

        if (claims.header[Header.TYPE] != TOKEN_TYPE_ACCESS) {
            throw InvalidTokenException
        }

        val userId = claims.body.subject?.toLongOrNull() ?: throw InvalidTokenException

        return UsernamePasswordAuthenticationToken(userId, "", emptyList())
    }

    fun validateRefreshToken(token: String): Long {
        val claims = getClaims(token)

        if (claims.header[Header.TYPE] != TOKEN_TYPE_REFRESH) {
            throw InvalidTokenException
        }

        val userId = claims.body.subject?.toLongOrNull() ?: throw InvalidTokenException

        val savedToken = refreshTokenRepository.findById(token)
            .orElseThrow { InvalidTokenException }

        if (savedToken.userId != userId) {
            throw InvalidTokenException
        }

        return userId
    }

    private fun getClaims(token: String): Jws<Claims> {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
        } catch (e: Exception) {
            logger.debug("Token validation failed: ${e.message}")
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(jwtProperties.header)
        return bearerToken?.takeIf { it.startsWith(jwtProperties.prefix) }
            ?.substring(jwtProperties.prefix.length)
    }
}
