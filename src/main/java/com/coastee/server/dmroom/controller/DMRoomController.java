package com.coastee.server.dmroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.dto.DMElements;
import com.coastee.server.dmroom.dto.DMRoomElements;
import com.coastee.server.dmroom.facade.DMRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dms")
public class DMRoomController {
    private final DMRoomFacade dmRoomFacade;

    @GetMapping("")
    @UserOnly
    public ApiResponse<DMRoomElements> getRooms(
            @Auth final Accessor accessor,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(dmRoomFacade.getRooms(accessor, pageable));
    }

    @GetMapping("/{roomId}")
    @UserOnly
    public ApiResponse<DMElements> getChats(
            @Auth final Accessor accessor,
            @PathVariable("roomId") final Long roomId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(dmRoomFacade.getChats(accessor, roomId, pageable));
    }
}
