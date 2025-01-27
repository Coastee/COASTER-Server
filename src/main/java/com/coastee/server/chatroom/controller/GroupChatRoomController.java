package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.dto.ChatRoomElement;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/servers/{serverId}/groups")
public class GroupChatRoomController {
    private final ChatRoomFacade chatRoomFacade;

    @GetMapping("")
    @UserOnly
    public ApiResponse<ChatRoomElement> getChatRooms(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestParam(value = "scope", required = false) Scope scope,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(chatRoomFacade.findByScope(accessor, serverId, scope, pageable));
    }

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> createGroupChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestBody final CreateGroupChatRequest request
    ) {
        chatRoomFacade.create(accessor, serverId, request);
        return ApiResponse.onSuccess();
    }
}
