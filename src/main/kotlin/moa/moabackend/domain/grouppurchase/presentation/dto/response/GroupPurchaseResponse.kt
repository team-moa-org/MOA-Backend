package moa.moabackend.domain.grouppurchase.presentation.dto.response

import moa.moabackend.domain.grouppurchase.domain.Category
import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import java.time.LocalDateTime


data class GroupPurchaseListResponse(
    val id: Long,
    val title: String,
    val category: Category,
    val thumbnailUrl: String,
    val basePrice: Int,
    val currentPrice: Int,
    val discountRate: Int, 
    val currentCount: Int,
    val remainingSeconds: Long, 
    val status: GroupPurchaseStatus
)

enum class SortType {
    LATEST,        
    DISCOUNT_RATE, 
    POPULARITY     
}


data class GroupPurchaseDetailResponse(
    val id: Long,
    val title: String,
    val category: Category,
    val thumbnailUrl: String,
    val content: String,
    val basePrice: Int,
    val currentPrice: Int,
    val targetCount: Int,
    val currentCount: Int,
    val deadline: LocalDateTime,
    val remainingSeconds: Long,
    val status: GroupPurchaseStatus,
    val totalRevenue: Int,       
    val achievementRate: Double, 
    val ownerName: String,
    val isOwner: Boolean,       
    val isJoined: Boolean,      
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

data class CategoryResponse(
    val category: Category,
    val displayName: String,
    val count: Long
)
