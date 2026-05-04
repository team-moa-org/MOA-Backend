package moa.moabackend.domain.grouppurchase.presentation.dto.response

data class GroupPurchaseCountUpdate(
    val groupPurchaseId: Long,
    val currentCount: Int,
    val currentPrice: Int,
    val status: String
)
