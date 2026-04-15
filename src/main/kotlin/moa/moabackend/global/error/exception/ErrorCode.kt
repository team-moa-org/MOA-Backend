package moa.moabackend.global.error.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val status: Int,
    val message: String
) {
    // server error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    // client error
    BAD_REQUEST(400, "Bad Request"),

    // jwt
    UNAUTHORIZED(401, "Unauthorized"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),

    // user
    USER_NOT_FOUND(404, "User not found"),
    USER_DELETED(403, "User deleted"),
    ;
}
