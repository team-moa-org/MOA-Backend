package moa.moabackend.domain.grouppurchase.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import moa.moabackend.domain.grouppurchase.presentation.dto.request.CreateGroupPurchaseRequest
import moa.moabackend.domain.grouppurchase.presentation.dto.response.CategoryResponse
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseDetailResponse
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseListResponse
import moa.moabackend.domain.grouppurchase.service.CreateGroupPurchaseService
import moa.moabackend.domain.grouppurchase.service.QueryCategoryListService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseDetailService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseListService
import moa.moabackend.domain.grouppurchase.service.CancelGroupPurchaseByAdminService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import moa.moabackend.domain.grouppurchase.domain.Category

import moa.moabackend.domain.grouppurchase.presentation.dto.response.SortType

@RestController
@RequestMapping("/group-purchase")
@Tag(name = "공동구매 API", description = "공동구매 게시글 생성, 조회, 참여를 담당합니다.")
class GroupPurchaseController(
    private val createGroupPurchaseService: CreateGroupPurchaseService,
    private val queryGroupPurchaseListService: QueryGroupPurchaseListService,
    private val queryGroupPurchaseDetailService: QueryGroupPurchaseDetailService,
    private val queryCategoryListService: QueryCategoryListService,
    private val cancelGroupPurchaseByAdminService: CancelGroupPurchaseByAdminService,
    private val objectMapper: ObjectMapper
) {

    @Operation(summary = "공동구매 게시글 생성 (ADMIN 전용)", security = [SecurityRequirement(name = "access-token")])
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestParam("data") data: String,
        @RequestPart("image") image: MultipartFile
    ) {
        val request = objectMapper.readValue(data, CreateGroupPurchaseRequest::class.java)
        createGroupPurchaseService.execute(request, image)
    }

    @Operation(summary = "공동구매 게시글 취소 (판매자 전용, 전원 환불 및 패널티)", security = [SecurityRequirement(name = "access-token")])
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelGroupByAdmin(@PathVariable id: Long) {
        cancelGroupPurchaseByAdminService.execute(id)
    }

    @Operation(summary = "공동구매 목록 조회 (비로그인 가능)")
    @GetMapping
    fun getList(
        @RequestParam(required = false) category: Category?,
        @RequestParam(required = false) sort: SortType?
    ): List<GroupPurchaseListResponse> {
        return queryGroupPurchaseListService.execute(category, sort)
    }

    @Operation(summary = "카테고리 목록 조회 (비로그인 가능)")
    @GetMapping("/categories")
    fun getCategories(): List<CategoryResponse> {
        return queryCategoryListService.execute()
    }

    @Operation(summary = "공동구매 상세 조회 (비로그인 가능)")
    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): GroupPurchaseDetailResponse {
        return queryGroupPurchaseDetailService.execute(id)
    }
}
