package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.facade.GroupChatRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/servers/{serverId}/groups")
public class GroupChatRoomController {
    private final GroupChatRoomFacade groupChatRoomFacade;

    @GetMapping("")
    @UserOnly
    public ApiResponse<ChatRoomElements> findByScope(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestParam(value = "scope", required = false) final Scope scope,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(groupChatRoomFacade.findByScope(accessor, serverId, scope, pageable));
    }

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> createGroupChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestPart @Valid final CreateGroupChatRequest request,
            @RequestPart final MultipartFile image
    ) {
        groupChatRoomFacade.create(accessor, serverId, request, image);
        return ApiResponse.onSuccess();
    }
}
