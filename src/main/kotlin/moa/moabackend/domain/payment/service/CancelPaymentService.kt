package moa.moabackend.domain.payment.service

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.exception.GroupPurchaseNotFoundException
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.payment.domain.PaymentStatus
import moa.moabackend.domain.payment.domain.exception.PaymentNotFoundException
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.infra.portone.PortOneClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CancelPaymentService(
    private val paymentRepository: PaymentRepository,
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val userFacade: UserFacade,
    private val portOneClient: PortOneClient
) {

    @Transactional
    fun execute(groupPurchaseId: Long) {
        val user = userFacade.getCurrentUser()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        // 1. 모집 중일 때만 취소 가능
        if (groupPurchase.status != GroupPurchaseStatus.RECRUITING) {
            throw RuntimeException("모집 중인 공동구매만 취소가 가능합니다.")
        }

        // 2. 결제 내역 찾기
        val payment = paymentRepository.findByUserAndGroupPurchaseAndStatus(user, groupPurchase, PaymentStatus.PAID)
            ?: throw PaymentNotFoundException

        // 3. 포트원 환불 호출
        val accessToken = portOneClient.getAccessToken()
        portOneClient.refund(
            impUid = payment.impUid!!,
            accessToken = accessToken,
            reason = "사용자 구매 취소",
            amount = payment.amount
        )

        // 4. 상태 변경 및 참여자 삭제
        payment.cancel()
        val participant = purchaseParticipantRepository.findByGroupPurchaseAndUser_Id(groupPurchase, user.id)
        participant?.let { purchaseParticipantRepository.delete(it) }

        // 5. 인원수 감소
        groupPurchase.leave(payment.quantity)
    }
}
