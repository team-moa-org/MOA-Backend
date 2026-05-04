package moa.moabackend.domain.payment.domain.repository

import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.payment.domain.Payment
import moa.moabackend.domain.payment.domain.PaymentStatus
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PaymentRepository : CrudRepository<Payment, Long> {
    fun findByMerchantUid(merchantUid: String): Payment?
    fun findAllByGroupPurchaseAndStatus(groupPurchase: GroupPurchase, status: PaymentStatus): List<Payment>
}
