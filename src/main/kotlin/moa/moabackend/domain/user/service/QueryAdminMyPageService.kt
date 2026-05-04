package moa.moabackend.domain.user.service

import moa.moabackend.domain.user.presentation.dto.response.AdminMyPageResponse
import moa.moabackend.domain.user.service.facade.UserFacade
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueryAdminMyPageService(
    private val userFacade: UserFacade
) {

    @Transactional(readOnly = true)
    fun execute(): AdminMyPageResponse {
        val user = userFacade.getCurrentUser()

        return AdminMyPageResponse(
            name = user.name,
            farmName = user.farmName,
            profileImageUrl = user.profileImageUrl,
            createdAt = user.createdAt
        )
    }
}
