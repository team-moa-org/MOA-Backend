package moa.moabackend.domain.user.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object PasswordMismatchException : MoaException(ErrorCode.PASSWORD_MISMATCH)
