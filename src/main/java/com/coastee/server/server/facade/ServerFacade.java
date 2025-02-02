package com.coastee.server.server.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.service.ChatService;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.service.HashTagService;
import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.dto.request.ServerEntryRequest;
import com.coastee.server.server.dto.response.ServerDetailResponse;
import com.coastee.server.server.service.NoticeService;
import com.coastee.server.server.service.ServerEntryService;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerEntryService serverEntryService;
    private final ChatRoomService chatRoomService;
    private final HashTagService hashTagService;
    private final ChatService chatService;
    private final NoticeService noticeService;

    public ServerElements findAll() {
        List<Server> serverList = serverService.findAll();
        return new ServerElements(serverList);
    }

    public ServerDetailResponse getHome(
            final Accessor accessor,
            final Long serverId
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.validateJoin(user, server);
        List<HashTag> hashTagList = hashTagService.findPopularTagByServer(
                server,
                PageRequest.of(0, 10)
        );
        Page<ChatRoom> groupPage = chatRoomService.findAllByServerAndType(
                server,
                ChatRoomType.GROUP,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        Page<ChatRoom> meetingPage = chatRoomService.findAllByServerAndType(
                server,
                ChatRoomType.MEETING,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "remainCount"))
        );
        Page<Notice> noticePage = noticeService.findAllByServer(
                server,
                PageRequest.of(0, 10)
        );
        ChatRoom serverChatRoom = chatRoomService
                .findAllByServerAndType(server, ChatRoomType.ENTIRE, PageRequest.of(0, 1))
                .getContent()
                .get(0);
        Page<Chat> chatPage = chatService.findAllByChatRoom(
                serverChatRoom,
                PageRequest.of(0, 10)
        );
        return new ServerDetailResponse(
                hashTagList,
                groupPage,
                meetingPage,
                noticePage,
                chatPage
        );
    }

    @Transactional
    public void enter(final Accessor accessor, final ServerEntryRequest request) {
        User user = userService.findById(accessor.getUserId());
        List<Server> serverList = serverService.findAllById(request.getIdList());
        serverEntryService.save(user, serverList);
    }

    @Transactional
    public void exit(final Accessor accessor, final Long serverId) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.exit(user, server);
    }
}
