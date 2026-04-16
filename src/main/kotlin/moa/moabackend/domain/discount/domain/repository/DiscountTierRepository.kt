package moa.moabackend.domain.discount.domain.repository

import moa.moabackend.domain.discount.domain.DiscountTier
import org.springframework.data.jpa.repository.JpaRepository

interface DiscountTierRepository : JpaRepository<DiscountTier, Long> {
}
