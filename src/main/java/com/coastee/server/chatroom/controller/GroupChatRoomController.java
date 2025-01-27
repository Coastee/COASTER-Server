package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/servers/{serverId}/groups")
public class GroupChatRoomController {
    private final ChatRoomFacade chatRoomFacade;

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> createGroupChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") Long serverId,
            @RequestBody CreateGroupChatRequest request
    ) {
        chatRoomFacade.create(accessor, serverId, request);
        return ApiResponse.onSuccess();
    }
}
