package moa.moabackend.domain.user.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import moa.moabackend.domain.user.presentation.dto.response.AdminMyPageResponse
import moa.moabackend.domain.user.service.QueryAdminMyPageService
import moa.moabackend.domain.user.service.WithdrawalService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "유저 관련 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
class UserController(
    private val queryAdminMyPageService: QueryAdminMyPageService,
    private val withdrawalService: WithdrawalService
) {

    @Operation(summary = "관리자 마이페이지 조회", security = [SecurityRequirement(name = "access-token")])
    @GetMapping("/admin")
    fun getAdminMyPage(): AdminMyPageResponse {
        return queryAdminMyPageService.execute()
    }

    @Operation(summary = "회원 탈퇴", security = [SecurityRequirement(name = "access-token")])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun withdrawal() {
        withdrawalService.execute()
    }

}
