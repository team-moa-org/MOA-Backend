package moa.moabackend.domain.payment.domain.exception

import moa.moabackend.global.error.exception.ErrorCode
import moa.moabackend.global.error.exception.MoaException

object PaymentNotFoundException : MoaException(ErrorCode.PAYMENT_NOT_FOUND)
object PaymentVerificationFailedException : MoaException(ErrorCode.PAYMENT_VERIFICATION_FAILED)
object PaymentAmountMismatchException : MoaException(ErrorCode.PAYMENT_AMOUNT_MISMATCH)
