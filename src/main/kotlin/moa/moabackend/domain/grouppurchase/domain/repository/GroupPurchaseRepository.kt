package moa.moabackend.domain.grouppurchase.domain.repository

import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface GroupPurchaseRepository : JpaRepository<GroupPurchase, Long> {
    fun findAllByStatusAndDeadlineBefore(status: GroupPurchaseStatus, deadline: LocalDateTime): List<GroupPurchase>
}
