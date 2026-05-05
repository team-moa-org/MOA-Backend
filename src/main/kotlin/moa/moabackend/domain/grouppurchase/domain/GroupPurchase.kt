package moa.moabackend.domain.grouppurchase.domain

import jakarta.persistence.*
import moa.moabackend.domain.discount.domain.DiscountTier
import moa.moabackend.domain.user.domain.User
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_group_purchase")
class GroupPurchase(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, length = 100)
    val title: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val category: Category,

    @Column(nullable = false, length = 255)
    val thumbnailUrl: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    val basePrice: Int,

    @Column(nullable = false)
    val targetCount: Int,

    @Column(nullable = false)
    var currentCount: Int = 0,

    @Column(nullable = false)
    val deadline: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: GroupPurchaseStatus = GroupPurchaseStatus.RECRUITING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "groupPurchase", cascade = [CascadeType.ALL], orphanRemoval = true)
    val discountTiers: MutableList<DiscountTier> = mutableListOf()

) {
    fun addDiscountTier(tier: DiscountTier) {
        discountTiers.add(tier)
    }

    fun join() {
        if (currentCount < targetCount) {
            currentCount++
        }
        if (currentCount >= targetCount) {
            status = GroupPurchaseStatus.COMPLETED
        }
    }

    // 현재 인원에 따른 실시간 가격 계산
    fun getCurrentPrice(): Int {
        // 인원수 조건에 맞는 티어 중 가장 큰 requiredCount를 가진 티어의 가격 선택
        return discountTiers
            .filter { currentCount >= it.requiredCount }
            .maxByOrNull { it.requiredCount }
            ?.discountPrice ?: basePrice
    }
}
