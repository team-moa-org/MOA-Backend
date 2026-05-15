package moa.moabackend.domain.participation.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import moa.moabackend.domain.participation.presentation.dto.response.ParticipantDetailResponse
import moa.moabackend.domain.participation.presentation.dto.response.ParticipantResponse
import moa.moabackend.domain.participation.service.QueryParticipantService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "참여자 관련 API", description = "참여자 관련 API")
@RestController
@RequestMapping("/participation")
class ParticipationController(
    private val queryParticipantService: QueryParticipantService
) {

    @Operation(summary = "참여자 목록 조회", security = [SecurityRequirement(name = "access-token")])
    @GetMapping("/{groupPurchaseId}")
    fun getParticipantList(@PathVariable groupPurchaseId: Long): List<ParticipantResponse> {
        return queryParticipantService.queryList(groupPurchaseId)
    }

    @Operation(summary = "참여자 상세 조회", security = [SecurityRequirement(name = "access-token")])
    @GetMapping("/{groupPurchaseId}/{userId}")
    fun getParticipantDetail(
        @PathVariable groupPurchaseId: Long,
        @PathVariable userId: Long
    ): ParticipantDetailResponse {
        return queryParticipantService.queryDetail(groupPurchaseId, userId)
    }
}
