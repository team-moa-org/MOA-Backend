package moa.moabackend.domain.user.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object UserNotFoundException : MoaException(ErrorCode.USER_NOT_FOUND)
