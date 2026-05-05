package moa.moabackend.domain.payment.domain

import jakarta.persistence.*
import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.user.domain.User
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_payment")
class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    val merchantUid: String,

    @Column
    var impUid: String? = null,

    @Column(nullable = false)
    val amount: Int,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false, length = 255)
    val shippingAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: PaymentStatus = PaymentStatus.READY,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_purchase_id", nullable = false)
    val groupPurchase: GroupPurchase,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

) {
    fun complete(impUid: String) {
        this.impUid = impUid
        this.status = PaymentStatus.PAID
    }

    fun fail() {
        this.status = PaymentStatus.FAILED
    }

    fun cancel() {
        this.status = PaymentStatus.CANCELLED
    }
}
