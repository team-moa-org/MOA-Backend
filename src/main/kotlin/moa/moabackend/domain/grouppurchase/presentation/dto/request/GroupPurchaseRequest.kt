package moa.moabackend.domain.grouppurchase.presentation.dto.request

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class CreateGroupPurchaseRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,

    @field:Min(1, message = "가격은 1원 이상이어야 합니다.")
    val basePrice: Int,

    @field:Min(2, message = "목표 인원은 최소 2명 이상이어야 합니다.")
    val targetCount: Int,

    @field:Future(message = "마감 기한은 미래 시점이어야 합니다.")
    val deadline: LocalDateTime,

    @field:NotEmpty(message = "최소 하나 이상의 할인 구간이 필요합니다.")
    val discountTiers: List<DiscountTierRequest>
)

data class DiscountTierRequest(
    @field:Min(1, message = "필요 인원수는 1명 이상이어야 합니다.")
    val requiredCount: Int,

    @field:Min(1, message = "할인 가격은 1원 이상이어야 합니다.")
    val discountPrice: Int
)
