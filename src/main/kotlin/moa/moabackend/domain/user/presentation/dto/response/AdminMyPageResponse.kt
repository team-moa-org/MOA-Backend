package moa.moabackend.domain.user.presentation.dto.response

import java.time.LocalDateTime

data class AdminMyPageResponse(
    val name: String,
    val farmName: String?,
    val profileImageUrl: String?,
    val createdAt: LocalDateTime
)
