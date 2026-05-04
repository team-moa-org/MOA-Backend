package moa.moabackend.domain.payment.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moa.moabackend.domain.payment.presentation.dto.request.PaymentVerifyRequest
import moa.moabackend.domain.payment.presentation.dto.response.PaymentReadyResponse
import moa.moabackend.domain.payment.service.PaymentService
import org.springframework.web.bind.annotation.*

@Tag(name = "결제 관련 API", description = "결제 관련 API")
@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService
) {

    @Operation(summary = "결제 준비 (merchant_uid 생성)")
    @PostMapping("/ready/{groupPurchaseId}")
    fun ready(@PathVariable groupPurchaseId: Long): PaymentReadyResponse {
        return paymentService.ready(groupPurchaseId)
    }

    @Operation(summary = "결제 검증 및 완료")
    @PostMapping("/verify")
    fun verify(@RequestBody request: PaymentVerifyRequest) {
        paymentService.verify(request)
    }
}
