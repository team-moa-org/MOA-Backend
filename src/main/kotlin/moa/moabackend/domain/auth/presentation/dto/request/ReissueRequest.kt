package moa.moabackend.domain.auth.presentation.dto.request

import jakarta.validation.constraints.NotBlank

data class ReissueRequest(
    @field:NotBlank(message = "refreshToken은 필수입니다.")
    val refreshToken: String
)
