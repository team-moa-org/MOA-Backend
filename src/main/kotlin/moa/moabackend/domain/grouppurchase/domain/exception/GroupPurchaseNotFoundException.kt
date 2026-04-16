package moa.moabackend.domain.grouppurchase.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object GroupPurchaseNotFoundException : MoaException(ErrorCode.GROUP_PURCHASE_NOT_FOUND)
