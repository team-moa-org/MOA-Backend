package moa.moabackend.domain.payment.domain.repository

import moa.moabackend.domain.payment.domain.Payment
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PaymentRepository : CrudRepository<Payment, Long> {
    fun findByMerchantUid(merchantUid: String): Payment?
}
