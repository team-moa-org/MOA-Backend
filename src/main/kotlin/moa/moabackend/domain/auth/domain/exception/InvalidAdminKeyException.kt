package moa.moabackend.domain.auth.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object InvalidAdminKeyException : MoaException(ErrorCode.INVALID_ADMIN_KEY)
