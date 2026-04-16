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
    USER_ALREADY_EXISTS(409, "User already exists"),
    PASSWORD_MISMATCH(401, "Password mismatch"),

    // group purchase
    GROUP_PURCHASE_NOT_FOUND(404, "Group purchase not found"),
    ALREADY_JOINED(409, "Already joined this group purchase"),
    OVER_TARGET_COUNT(400, "Already reached target count"),
    EXPIRED_DEADLINE(400, "Deadline has passed"),
    CANNOT_JOIN_OWN_PURCHASE(400, "Cannot join your own group purchase"),

    // auth
    INVALID_ADMIN_KEY(401, "Invalid admin key"),
    ;
}
