package moa.moabackend.domain.grouppurchase.domain.repository

import moa.moabackend.domain.grouppurchase.domain.GroupPurchase
import org.springframework.data.jpa.repository.JpaRepository

interface GroupPurchaseRepository : JpaRepository<GroupPurchase, Long> {
}
