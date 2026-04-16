package moa.moabackend.domain.discount.domain

import jakarta.persistence.*
import moa.moabackend.domain.grouppurchase.domain.GroupPurchase

@Entity
@Table(name = "tbl_discount_tier")
class DiscountTier(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_purchase_id", nullable = false)
    val groupPurchase: GroupPurchase,

    @Column(nullable = false)
    val requiredCount: Int,

    @Column(nullable = false)
    val discountPrice: Int

)
