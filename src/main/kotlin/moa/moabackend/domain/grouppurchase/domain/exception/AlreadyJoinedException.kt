package moa.moabackend.domain.grouppurchase.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object AlreadyJoinedException : MoaException(ErrorCode.ALREADY_JOINED)
