package com.coastee.server.server.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.service.ChatService;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.service.HashTagService;
import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.dto.response.ServerHomeResponse;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerEntryService serverEntryService;
    private final ChatRoomEntryService chatRoomEntryService;
    private final ChatRoomService chatRoomService;
    private final HashTagService hashTagService;
    private final ChatService chatService;
    private final NoticeService noticeService;

    public ServerElements findAll() {
        List<Server> serverList = serverService.findAll();
        return new ServerElements(serverList);
    }

    public Long findJoinServer(final Accessor accessor) {
        User user = userService.findById(accessor.getUserId());
        ServerEntry entry = serverEntryService.findAnyByUser(user);
        return entry.getServer().getId();
    }

    public ServerHomeResponse getHomeWithConditions(
            final Accessor accessor,
            final Long serverId,
            final String keyword,
            final List<String> tagNameList
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.validateJoin(user, server);

        List<HashTag> hashTagList = hashTagService.findPopularTagByServer(
                server,
                PageRequest.of(0, 10)
        );

        List<HashTag> searchTagList = new ArrayList<>();
        if (tagNameList != null && !tagNameList.isEmpty())
            searchTagList = hashTagService.findAllByContentIn(new HashSet<>(tagNameList));
        Page<ChatRoom> groupPage = chatRoomService.findByServerAndTypeAndKeywordAndTagList(
                server,
                ChatRoomType.GROUP,
                keyword,
                searchTagList,
                PageRequest.of(0, 3, Sort.by(DESC, "createdDate"))
        );
        Page<ChatRoom> meetingPage = chatRoomService.findByServerAndTypeAndKeywordAndTagList(
                server,
                ChatRoomType.MEETING,
                keyword,
                searchTagList,
                PageRequest.of(0, 3, Sort.by(DESC, "remainCount"))
        );
        if (isSearch(keyword, tagNameList))
            return new ServerHomeResponse(hashTagList, groupPage, meetingPage);

        Page<Notice> noticePage = noticeService.findAllByServer(
                server,
                PageRequest.of(0, 10, Sort.by(DESC, "createdDate"))
        );
        ChatRoom serverChatRoom = chatRoomService.findEntireChatRoomByServer(server);
        Page<Chat> chatPage = chatService.findAllByChatRoom(
                serverChatRoom,
                PageRequest.of(0, 10, Sort.by(DESC, "createdDate"))
        );
        return new ServerHomeResponse(
                hashTagList,
                groupPage,
                meetingPage,
                noticePage,
                chatPage
        );
    }

    private boolean isSearch(final String keyword, final List<String> tagNameList) {
        return keyword != null || (tagNameList != null && !tagNameList.isEmpty());
    }

    @Transactional
    public void enter(final Accessor accessor, final Long serverId) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.enter(user, server);
        ChatRoom serverChatRoom = chatRoomService.findEntireChatRoomByServer(server);
        chatRoomEntryService.enter(user, serverChatRoom);
    }

    @Transactional
    public void exit(final Accessor accessor, final Long serverId) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.exit(user, server);
        chatRoomService.exit(user, server);
        chatRoomEntryService.exit(user, server);
    }
}
