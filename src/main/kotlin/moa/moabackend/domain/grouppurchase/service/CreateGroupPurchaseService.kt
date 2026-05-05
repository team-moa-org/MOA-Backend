package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.discount.domain.DiscountTier
import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.grouppurchase.presentation.dto.request.CreateGroupPurchaseRequest
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.global.config.S3UploadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CreateGroupPurchaseService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val userFacade: UserFacade,
    private val s3UploadService: S3UploadService
) {

    @Transactional
    fun execute(request: CreateGroupPurchaseRequest, image: MultipartFile) {
        val user = userFacade.getCurrentUser()
        val thumbnailUrl = s3UploadService.upload(image)

        val groupPurchase = GroupPurchase(
            title = request.title,
            category = request.category,
            thumbnailUrl = thumbnailUrl,
            content = request.content,
            basePrice = request.basePrice,
            targetCount = request.targetCount,
            deadline = request.deadline,
            user = user
        )

        request.discountTiers.forEach {
            groupPurchase.addDiscountTier(
                DiscountTier(
                    groupPurchase = groupPurchase,
                    requiredCount = it.requiredCount,
                    discountPrice = it.discountPrice
                )
            )
        }

        groupPurchaseRepository.save(groupPurchase)
    }
}
