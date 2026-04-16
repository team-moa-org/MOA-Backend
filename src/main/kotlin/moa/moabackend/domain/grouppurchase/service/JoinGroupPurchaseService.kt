package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.exception.*
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.PurchaseParticipant
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.user.service.facade.UserFacade
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class JoinGroupPurchaseService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val userFacade: UserFacade
) {

    @Transactional
    fun execute(groupPurchaseId: Long) {
        val user = userFacade.getCurrentUser()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        // 마감 기한 확인
        if (groupPurchase.deadline.isBefore(LocalDateTime.now())) {
            throw ExpiredDeadlineException
        }

        // 모집 상태 확인
        if (groupPurchase.status != GroupPurchaseStatus.RECRUITING) {
            throw OverTargetCountException
        }

        // 본인 게시글 참여 제한
        if (groupPurchase.user.id == user.id) {
            throw CannotJoinOwnPurchaseException
        }

        // 중복 참여 확인
        if (purchaseParticipantRepository.existsByUserAndGroupPurchase(user, groupPurchase)) {
            throw AlreadyJoinedException
        }

        // 참여 데이터 저장
        purchaseParticipantRepository.save(
            PurchaseParticipant(
                user = user,
                groupPurchase = groupPurchase
            )
        )

        // 엔티티 상태 업데이트 (인원 증가 및 상태 변경)
        groupPurchase.join()
    }
}
