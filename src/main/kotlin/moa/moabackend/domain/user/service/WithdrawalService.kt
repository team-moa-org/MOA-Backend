package moa.moabackend.domain.user.service

import moa.moabackend.domain.user.service.facade.UserFacade
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WithdrawalService(
    private val userFacade: UserFacade
) {

    @Transactional
    fun execute() {
        val user = userFacade.getCurrentUser()
        user.softDelete()
    }
}
