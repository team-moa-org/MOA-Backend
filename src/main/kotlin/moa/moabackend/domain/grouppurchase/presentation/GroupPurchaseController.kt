package moa.moabackend.domain.grouppurchase.presentation

import jakarta.validation.Valid
import moa.moabackend.domain.grouppurchase.presentation.dto.request.CreateGroupPurchaseRequest
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseDetailResponse
import moa.moabackend.domain.grouppurchase.presentation.dto.response.GroupPurchaseListResponse
import moa.moabackend.domain.grouppurchase.service.CreateGroupPurchaseService
import moa.moabackend.domain.grouppurchase.service.JoinGroupPurchaseService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseDetailService
import moa.moabackend.domain.grouppurchase.service.QueryGroupPurchaseListService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/group-purchase")
class GroupPurchaseController(
    private val createGroupPurchaseService: CreateGroupPurchaseService,
    private val joinGroupPurchaseService: JoinGroupPurchaseService,
    private val queryGroupPurchaseListService: QueryGroupPurchaseListService,
    private val queryGroupPurchaseDetailService: QueryGroupPurchaseDetailService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestPart("data") @Valid request: CreateGroupPurchaseRequest,
        @RequestPart("image") image: MultipartFile
    ) {
        createGroupPurchaseService.execute(request, image)
    }

    @PostMapping("/{id}/join")
    fun join(@PathVariable id: Long) {
        joinGroupPurchaseService.execute(id)
    }

    @GetMapping
    fun getList(): List<GroupPurchaseListResponse> {
        return queryGroupPurchaseListService.execute()
    }

    @GetMapping("/{id}")
    fun getDetail(@PathVariable id: Long): GroupPurchaseDetailResponse {
        return queryGroupPurchaseDetailService.execute(id)
    }
}
