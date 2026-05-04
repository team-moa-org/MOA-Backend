package moa.moabackend.domain.payment.service

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.exception.*
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.PurchaseParticipant
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.payment.domain.Payment
import moa.moabackend.domain.payment.domain.exception.PaymentAmountMismatchException
import moa.moabackend.domain.payment.domain.exception.PaymentNotFoundException
import moa.moabackend.domain.payment.domain.exception.PaymentVerificationFailedException
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.payment.presentation.dto.request.PaymentVerifyRequest
import moa.moabackend.domain.payment.presentation.dto.response.PaymentReadyResponse
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.infra.portone.PortOneClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val userFacade: UserFacade,
    private val portOneClient: PortOneClient
) {

    @Transactional
    fun ready(groupPurchaseId: Long): PaymentReadyResponse {
        val user = userFacade.getCurrentUser()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        // 참여 가능 여부 사전 체크
        validateJoinable(user.id, groupPurchase)

        val merchantUid = "order_${UUID.randomUUID()}"
        val amount = groupPurchase.getCurrentPrice()

        paymentRepository.save(
            Payment(
                merchantUid = merchantUid,
                amount = amount,
                user = user,
                groupPurchase = groupPurchase
            )
        )

        return PaymentReadyResponse(merchantUid, amount)
    }

    @Transactional
    fun verify(request: PaymentVerifyRequest) {
        val payment = paymentRepository.findByMerchantUid(request.merchantUid)
            ?: throw PaymentNotFoundException

        val accessToken = portOneClient.getAccessToken()
        val portOnePayment = portOneClient.getPaymentData(request.impUid, accessToken)

        // 1. 금액 검증
        if (payment.amount != portOnePayment.amount) {
            payment.fail()
            throw PaymentAmountMismatchException
        }

        // 2. 상태 확인
        if (portOnePayment.status != "paid") {
            payment.fail()
            throw PaymentVerificationFailedException
        }

        // 3. 결제 완료 처리
        payment.complete(request.impUid)

        // 4. 공동구매 참여 처리 (기존 JoinGroupPurchaseService 로직 통합)
        finalizeJoin(payment)
    }

    private fun validateJoinable(userId: Long, groupPurchase: moa.moabackend.domain.grouppurchase.domain.GroupPurchase) {
        if (groupPurchase.deadline.isBefore(LocalDateTime.now())) {
            throw ExpiredDeadlineException
        }
        if (groupPurchase.status != GroupPurchaseStatus.RECRUITING) {
            throw OverTargetCountException
        }
        if (groupPurchase.user.id == userId) {
            throw CannotJoinOwnPurchaseException
        }
        val user = userFacade.findByUserId(userId)
        if (purchaseParticipantRepository.existsByUserAndGroupPurchase(user, groupPurchase)) {
            throw AlreadyJoinedException
        }
    }

    private fun finalizeJoin(payment: Payment) {
        purchaseParticipantRepository.save(
            PurchaseParticipant(
                user = payment.user,
                groupPurchase = payment.groupPurchase
            )
        )
        payment.groupPurchase.join()
    }
}
