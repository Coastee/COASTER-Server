package com.coastee.server.chatroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElement;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.global.util.PageableUtil;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.chatroom.domain.ChatRoomType.GROUP;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupChatRoomFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomEntryService chatRoomEntryService;

    @Transactional
    public void create(
            final Accessor accessor,
            final Long serverId,
            final CreateGroupChatRequest request
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        ChatRoom chatRoom = chatRoomService.save(
                new ChatRoom(server, user, request.getTitle(), request.getContent(), "", GROUP)
        );
        chatRoomEntryService.save(new ChatRoomEntry(user, chatRoom));
    }

    public ChatRoomElement findByScope(
            final Accessor accessor,
            final Long serverId,
            final Scope scope,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        Page<ChatRoom> chatRoomList;
        if (scope.equals(Scope.all))
            chatRoomList = chatRoomService.findAllByServerAndType(
                    server,
                    GROUP,
                    PageableUtil.setSortOrder(pageable)
            );
        if (scope.equals(Scope.joined)) {
            chatRoomList = chatRoomService.findAllByServerAndUserAndType(
                    server,
                    user,
                    GROUP,
                    PageableUtil.setSortOrder(pageable)
            );
        }
        return null;
    }
}
