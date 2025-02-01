package com.coastee.server.chatroom.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.CreateChatRoomRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;
import static com.coastee.server.global.util.PageableUtil.setChatOrder;
import static com.coastee.server.global.util.PageableUtil.setChatRoomOrder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/servers/{serverId}/{chatRoomType}")
public class ChatRoomController {
    private final ChatRoomFacade chatRoomFacade;

    @GetMapping("")
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
                setChatRoomOrder(pageable)
        ));
    }

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> createChatRoom(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @RequestPart @Valid final CreateChatRoomRequest request,
            @RequestPart final MultipartFile image
    ) {
        chatRoomFacade.create(accessor, serverId, chatRoomType, request, image);
        return ApiResponse.onSuccess();
    }

    @GetMapping("/{chatRoomId}")
    @UserOnly
    public ApiResponse<ChatElements> getChats(
            @Auth final Accessor accessor,
            @PathVariable("serverId") final Long serverId,
            @PathVariable("chatRoomType") final ChatRoomType chatRoomType,
            @PathVariable("chatRoomId") final Long chatRoomId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(chatRoomFacade.getChats(accessor, chatRoomId, setChatOrder(pageable)));
    }

    @PostMapping("/{chatRoomId}")
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

    @DeleteMapping("/{chatRoomId}")
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
