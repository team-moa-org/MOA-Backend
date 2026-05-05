package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.exception.GroupPurchaseNotFoundException
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.grouppurchase.presentation.dto.response.*
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.payment.domain.PaymentStatus
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.user.service.facade.UserFacade
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class QueryGroupPurchaseDetailService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val paymentRepository: PaymentRepository,
    private val userFacade: UserFacade
) {

    @Transactional(readOnly = true)
    fun execute(id: Long): GroupPurchaseDetailResponse {
        val user = userFacade.getCurrentUserOrNull()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(id)
            ?: throw GroupPurchaseNotFoundException

        val now = LocalDateTime.now()
        val currentCount = groupPurchase.currentCount

        // 매출 및 달성률 계산
        val totalRevenue = paymentRepository.findAllByGroupPurchaseAndStatus(groupPurchase, PaymentStatus.PAID)
            .sumOf { it.amount }
        val achievementRate = (currentCount.toDouble() / groupPurchase.targetCount.toDouble()) * 100

        // 다음 할인 단계 찾기
        val nextTier = groupPurchase.discountTiers
            .filter { it.requiredCount > currentCount }
            .minByOrNull { it.requiredCount }

        val nextDiscount = nextTier?.let {
            NextDiscountResponse(
                remainingCount = it.requiredCount - currentCount,
                nextPrice = it.discountPrice
            )
        }

        return GroupPurchaseDetailResponse(
            id = groupPurchase.id,
            title = groupPurchase.title,
            category = groupPurchase.category,
            thumbnailUrl = groupPurchase.thumbnailUrl,
            content = groupPurchase.content,
            basePrice = groupPurchase.basePrice,
            currentPrice = groupPurchase.getCurrentPrice(),
            targetCount = groupPurchase.targetCount,
            currentCount = groupPurchase.currentCount,
            deadline = groupPurchase.deadline,
            remainingSeconds = if (groupPurchase.deadline.isAfter(now)) {
                Duration.between(now, groupPurchase.deadline).seconds
            } else 0L,
            status = groupPurchase.status,
            totalRevenue = totalRevenue,
            achievementRate = achievementRate,
            ownerName = groupPurchase.user.name,
            isOwner = user?.let { it.id == groupPurchase.user.id } ?: false,
            isJoined = user?.let { purchaseParticipantRepository.existsByUserAndGroupPurchase(it, groupPurchase) } ?: false,
            discountTiers = groupPurchase.discountTiers.map {
                DiscountTierResponse(it.requiredCount, it.discountPrice)
            },
            nextDiscount = nextDiscount
        )
    }
}
