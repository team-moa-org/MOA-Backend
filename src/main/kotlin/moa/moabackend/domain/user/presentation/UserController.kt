package moa.moabackend.domain.user.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import moa.moabackend.domain.user.presentation.dto.response.AdminMyPageResponse
import moa.moabackend.domain.user.service.QueryAdminMyPageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "유저 관련 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
class UserController(
    private val queryAdminMyPageService: QueryAdminMyPageService
) {

    @Operation(summary = "관리자 마이페이지 조회")
    @GetMapping("/admin")
    fun getAdminMyPage(): AdminMyPageResponse {
        return queryAdminMyPageService.execute()
    }

}
