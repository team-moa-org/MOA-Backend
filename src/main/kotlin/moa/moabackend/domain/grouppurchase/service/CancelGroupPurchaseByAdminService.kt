package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.exception.GroupPurchaseNotFoundException
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.payment.domain.PaymentStatus
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.infra.portone.PortOneClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CancelGroupPurchaseByAdminService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val paymentRepository: PaymentRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val userFacade: UserFacade,
    private val portOneClient: PortOneClient
) {

    @Transactional
    fun execute(groupPurchaseId: Long) {
        val admin = userFacade.getCurrentUser()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        // 본인의 게시글인지 확인
        if (groupPurchase.user.id != admin.id) {
            throw RuntimeException("본인의 공동구매 게시글만 취소할 수 있습니다.")
        }

        // 이미 완료되거나 취소된 경우 확인
        if (groupPurchase.status == GroupPurchaseStatus.CANCELLED) {
            throw RuntimeException("이미 취소된 공동구매입니다.")
        }

        // 1. 참여자 전원 환불 처리
        val payments = paymentRepository.findAllByGroupPurchaseAndStatus(groupPurchase, PaymentStatus.PAID)
        val accessToken = portOneClient.getAccessToken()

        payments.forEach { payment ->
            portOneClient.refund(
                impUid = payment.impUid!!,
                accessToken = accessToken,
                reason = "판매자에 의한 공동구매 취소",
                amount = payment.amount
            )
            payment.cancel()
        }

        // 2. 참여자 데이터 삭제
        val participants = purchaseParticipantRepository.findAllByGroupPurchase(groupPurchase)
        purchaseParticipantRepository.deleteAll(participants)

        // 3. 게시글 상태 변경
        groupPurchase.status = GroupPurchaseStatus.CANCELLED
        groupPurchaseRepository.save(groupPurchase)

        // 4. 판매자 패널티 부여
        applyPenalty(admin)
    }

    private fun applyPenalty(admin: moa.moabackend.domain.user.domain.User) {
        admin.penaltyCount += 1
        
        val now = LocalDateTime.now()
        when (admin.penaltyCount) {
            1 -> {
                // 1회: 경고
            }
            2 -> {
                // 2회: 3일 정지 (게시글 생성 제한)
                admin.suspendedUntil = now.plusDays(3)
            }
            3 -> {
                // 3회: 3일 정지 (모든 기능 제한)
                admin.suspendedUntil = now.plusDays(3)
            }
            else -> {
                // 4회 이상: 영구 정지
                admin.suspendedUntil = now.plusYears(100)
            }
        }
    }
}
