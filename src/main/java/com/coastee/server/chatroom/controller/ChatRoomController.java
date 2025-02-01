package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.dto.request.CreateChatRoomRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/servers/{serverId}/{chatRoomType}")
public class ChatRoomController {
    private final ChatRoomFacade chatRoomFacade;

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> createGroupChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @RequestPart @Valid final CreateChatRoomRequest request,
            @RequestPart final MultipartFile image
    ) {
        chatRoomFacade.create(accessor, serverId, request, image);
        return ApiResponse.onSuccess();
    }
}
