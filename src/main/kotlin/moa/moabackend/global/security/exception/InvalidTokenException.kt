package moa.moabackend.global.security.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object InvalidTokenException : MoaException(ErrorCode.INVALID_TOKEN)
