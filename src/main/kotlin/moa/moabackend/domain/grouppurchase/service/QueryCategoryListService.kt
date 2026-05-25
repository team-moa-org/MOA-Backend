package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.Category
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.grouppurchase.presentation.dto.response.CategoryResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueryCategoryListService(
    private val groupPurchaseRepository: GroupPurchaseRepository
) {

    @Transactional(readOnly = true)
    fun execute(): List<CategoryResponse> {
        return Category.entries.map {
            CategoryResponse(
                category = it,
                displayName = it.displayName,
                count = groupPurchaseRepository.countByCategory(it)
            )
        }
    }
}
