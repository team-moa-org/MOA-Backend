package moa.moabackend.global.error.exception

abstract class MoaException(
    val errorCode: ErrorCode
) : RuntimeException()
