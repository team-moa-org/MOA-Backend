package moa.moabackend.domain.participation.domain

import jakarta.persistence.*
import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.user.domain.User
import java.time.LocalDateTime

@Entity
@Table(
    name = "tbl_purchase_participant",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "group_purchase_id"])
    ]
)
class PurchaseParticipant(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_purchase_id", nullable = false)
    val groupPurchase: GroupPurchase,

    @Column(nullable = false)
    val joinedAt: LocalDateTime = LocalDateTime.now()

)
