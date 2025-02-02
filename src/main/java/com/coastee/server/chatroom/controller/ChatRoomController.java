package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.ChatRoomCreateRequest;
import com.coastee.server.chatroom.dto.request.MeetingCreateRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
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
@RequestMapping("/api/v1/servers/{serverId}")
public class ChatRoomController {
    private final ChatRoomFacade chatRoomFacade;

    @GetMapping("/{chatRoomType}")
    @UserOnly
    public ApiResponse<ChatRoomElements> findByScope(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @RequestParam(value = "scope", required = false) final Scope scope,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(chatRoomFacade.findByScope(
                accessor,
                serverId,
                chatRoomType,
                scope,
                pageable
        ));
    }

    @PostMapping("/groups")
    @UserOnly
    public ApiResponse<Void> createGroup(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestPart @Valid final ChatRoomCreateRequest request,
            @RequestPart final MultipartFile image
    ) {
        chatRoomFacade.create(accessor, serverId, ChatRoomType.GROUP, request, image);
        return ApiResponse.onSuccess();
    }

    @PostMapping("/meetings")
    @UserOnly
    public ApiResponse<Void> createMeeting(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @RequestPart @Valid final MeetingCreateRequest request,
            @RequestPart final MultipartFile image
    ) {
        chatRoomFacade.create(accessor, serverId, ChatRoomType.MEETING, request, image);
        return ApiResponse.onSuccess();
    }

    @GetMapping("/{chatRoomType}/{chatRoomId}")
    @UserOnly
    public ApiResponse<ChatElements> getChats(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @PathVariable("chatRoomId") final Long chatRoomId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(chatRoomFacade.getChats(accessor, chatRoomId, pageable));
    }

    @PostMapping("/{chatRoomType}/{chatRoomId}")
    @UserOnly
    public ApiResponse<Void> enterChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @PathVariable("chatRoomId") final Long chatRoomId
    ) {
        chatRoomFacade.enter(accessor, chatRoomId);
        return ApiResponse.onSuccess();
    }

    @DeleteMapping("/{chatRoomType}/{chatRoomId}")
    @UserOnly
    public ApiResponse<Void> exitChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @PathVariable("chatRoomId") final Long chatRoomId
    ) {
        chatRoomFacade.exit(accessor, chatRoomId);
        return ApiResponse.onSuccess();
    }
}
