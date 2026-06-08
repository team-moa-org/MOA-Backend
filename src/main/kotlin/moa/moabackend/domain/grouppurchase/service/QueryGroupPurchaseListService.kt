package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.Category
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseListResponse
import moa.moabackend.domain.grouppurchase.presentation.dto.response.SortType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class QueryGroupPurchaseListService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val s3UploadService: moa.moabackend.global.config.S3UploadService
) {

    @Transactional(readOnly = true)
    fun execute(category: Category?, sort: SortType?): List<GroupPurchaseListResponse> {
        val now = LocalDateTime.now()

        val entities = if (category != null) {
            groupPurchaseRepository.findAllByCategoryOrderByIdDesc(category)
        } else {
            groupPurchaseRepository.findAll()
        }

        val responseList = entities.map {
            val currentPrice = it.getCurrentPrice()
            val discountRate = ((it.basePrice - currentPrice).toDouble() / it.basePrice * 100).toInt()

            GroupPurchaseListResponse(
                id = it.id,
                title = it.title,
                category = it.category,
                thumbnailUrl = s3UploadService.generatePresignedUrl(it.thumbnailUrl) ?: "",
                basePrice = it.basePrice,
                currentPrice = currentPrice,
                discountRate = discountRate,
                currentCount = it.currentCount,
                remainingSeconds = if (it.deadline.isAfter(now)) {
                    Duration.between(now, it.deadline).seconds
                } else 0L,
                status = it.status
            )
        }

        return when (sort ?: SortType.LATEST) {
            SortType.LATEST -> responseList.sortedByDescending { it.id }
            SortType.DISCOUNT_RATE -> responseList.sortedByDescending { it.discountRate }
            SortType.POPULARITY -> responseList.sortedByDescending { it.currentCount }
        }
    }
}
