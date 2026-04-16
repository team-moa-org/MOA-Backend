package moa.moabackend.domain.grouppurchase.presentation.dto.response

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import java.time.LocalDateTime

/**
 * 목록 조회용 (간결한 정보)
 */
data class GroupPurchaseListResponse(
    val id: Long,
    val title: String,
    val thumbnailUrl: String,
    val basePrice: Int,
    val currentPrice: Int,
    val currentCount: Int,
    val remainingSeconds: Long, // 프론트 카운트다운용
    val status: GroupPurchaseStatus
)

/**
 * 상세 조회용 (모든 정보)
 */
data class GroupPurchaseDetailResponse(
    val id: Long,
    val title: String,
    val thumbnailUrl: String,
    val content: String,
    val basePrice: Int,
    val currentPrice: Int,
    val targetCount: Int,
    val currentCount: Int,
    val deadline: LocalDateTime,
    val remainingSeconds: Long,
    val status: GroupPurchaseStatus,
    val ownerName: String,
    val isOwner: Boolean,       // 현재 사용자가 주인인지
    val isJoined: Boolean,      // 현재 사용자가 이미 참여했는지
    val discountTiers: List<DiscountTierResponse>,
    val nextDiscount: NextDiscountResponse?
)

data class DiscountTierResponse(
    val requiredCount: Int,
    val discountPrice: Int
)

data class NextDiscountResponse(
    val remainingCount: Int,
    val nextPrice: Int
)
