package com.coastee.server.hashtag.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.hashtag.dto.HashTagElements;
import com.coastee.server.hashtag.facade.HashTagFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers/{serverId}/tags")
public class HashTagController {
    private final HashTagFacade hashTagFacade;

    @GetMapping("")
    public ApiResponse<HashTagElements> findAll(
            @PathVariable final Long serverId,
            @RequestParam(value = "keyword", required = false) final String keyword,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(hashTagFacade.findWithConditions(
                serverId,
                keyword,
                pageable
        ));
    }
}
