package moa.moabackend.domain.payment.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import moa.moabackend.domain.payment.presentation.dto.request.PaymentReadyRequest
import moa.moabackend.domain.payment.presentation.dto.request.PaymentVerifyRequest
import moa.moabackend.domain.payment.presentation.dto.response.PaymentReadyResponse
import moa.moabackend.domain.payment.service.CancelPaymentService
import moa.moabackend.domain.payment.service.PaymentService
import org.springframework.web.bind.annotation.*

@Tag(name = "결제 관련 API", description = "결제 관련 API")
@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService,
    private val cancelPaymentService: CancelPaymentService
) {

    @Operation(summary = "결제 준비 (merchant_uid 생성)", security = [SecurityRequirement(name = "access-token")])
    @PostMapping("/ready/{groupPurchaseId}")
    fun ready(
        @PathVariable groupPurchaseId: Long,
        @RequestBody request: PaymentReadyRequest
    ): PaymentReadyResponse {
        return paymentService.ready(groupPurchaseId, request)
    }

    @Operation(summary = "결제 검증 및 완료", security = [SecurityRequirement(name = "access-token")])
    @PostMapping("/verify")
    fun verify(@RequestBody request: PaymentVerifyRequest) {
        paymentService.verify(request)
    }

    @Operation(summary = "참여 취소 및 환불", security = [SecurityRequirement(name = "access-token")])
    @PostMapping("/cancel/{groupPurchaseId}")
    fun cancel(@PathVariable groupPurchaseId: Long) {
        cancelPaymentService.execute(groupPurchaseId)
    }
}
