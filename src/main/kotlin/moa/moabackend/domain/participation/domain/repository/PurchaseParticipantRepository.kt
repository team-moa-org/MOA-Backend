package moa.moabackend.domain.participation.domain.repository

import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.participation.domain.PurchaseParticipant
import moa.moabackend.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseParticipantRepository : JpaRepository<PurchaseParticipant, Long> {
    fun existsByUserAndGroupPurchase(user: User, groupPurchase: GroupPurchase): Boolean
}
