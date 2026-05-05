package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseListResponse
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class QueryGroupPurchaseListService(
    private val groupPurchaseRepository: GroupPurchaseRepository
) {

    @Transactional(readOnly = true)
    fun execute(): List<GroupPurchaseListResponse> {
        val now = LocalDateTime.now()

        return groupPurchaseRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .map {
                GroupPurchaseListResponse(
                    id = it.id,
                    title = it.title,
                    category = it.category,
                    thumbnailUrl = it.thumbnailUrl,
                    basePrice = it.basePrice,
                    currentPrice = it.getCurrentPrice(),
                    currentCount = it.currentCount,
                    remainingSeconds = if (it.deadline.isAfter(now)) {
                        Duration.between(now, it.deadline).seconds
                    } else 0L,
                    status = it.status
                )
            }
    }
}
