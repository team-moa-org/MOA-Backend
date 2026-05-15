package moa.moabackend.domain.grouppurchase.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import moa.moabackend.domain.grouppurchase.presentation.dto.request.CreateGroupPurchaseRequest
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseDetailResponse
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseListResponse
import moa.moabackend.domain.grouppurchase.service.CreateGroupPurchaseService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseDetailService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseListService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/group-purchase")
@Tag(name = "공동구매 API", description = "공동구매 게시글 생성, 조회, 참여를 담당합니다.")
class GroupPurchaseController(
    private val createGroupPurchaseService: CreateGroupPurchaseService,
    private val queryGroupPurchaseListService: QueryGroupPurchaseListService,
    private val queryGroupPurchaseDetailService: QueryGroupPurchaseDetailService
) {

    @Operation(summary = "공동구매 게시글 생성 (ADMIN 전용)", security = [SecurityRequirement(name = "access-token")])
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestPart("data") @Valid request: CreateGroupPurchaseRequest,
        @RequestPart("image") image: MultipartFile
    ) {
        createGroupPurchaseService.execute(request, image)
    }

    @Operation(summary = "공동구매 목록 조회 (비로그인 가능)")
    @GetMapping
    fun getList(): List<GroupPurchaseListResponse> {
        return queryGroupPurchaseListService.execute()
    }

    @Operation(summary = "공동구매 상세 조회 (비로그인 가능)")
    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): GroupPurchaseDetailResponse {
        return queryGroupPurchaseDetailService.execute(id)
    }
}
