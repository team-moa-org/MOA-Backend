package moa.moabackend.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import moa.moabackend.global.error.CustomAuthenticationEntryPoint
import moa.moabackend.global.security.jwt.JwtFilter
import moa.moabackend.global.security.jwt.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                    .requestMatchers(HttpMethod.GET, "/group-purchase/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/group-purchase").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/group-purchase/*/join").authenticated()
                    .requestMatchers("/user/admin").hasRole("ADMIN")
                    .requestMatchers("/payment/**").authenticated()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
