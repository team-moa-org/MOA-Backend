package moa.moabackend.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import moa.moabackend.global.security.exception.ExpiredTokenException
import moa.moabackend.global.security.exception.InvalidTokenException

@Component
class JwtFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.method == "OPTIONS") {
            filterChain.doFilter(request, response)
            return
        }

        val token = jwtTokenProvider.resolveToken(request)

        if (token != null) {
            try {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
                log.debug(
                    "Set Authentication to security context for '{}', uri: {}",
                    authentication.name,
                    request.requestURI
                )
            } catch (e: ExpiredTokenException) {
                request.setAttribute("exception", e.errorCode)
                SecurityContextHolder.clearContext()
                filterChain.doFilter(request, response)
                return
            } catch (e: InvalidTokenException) {
                request.setAttribute("exception", e.errorCode)
                SecurityContextHolder.clearContext()
                filterChain.doFilter(request, response)
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
