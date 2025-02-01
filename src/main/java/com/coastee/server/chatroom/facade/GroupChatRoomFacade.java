package com.coastee.server.chatroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.hashtag.service.HashTagService;
import com.coastee.server.image.domain.DirName;
import com.coastee.server.image.service.BlobStorageService;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.coastee.server.chatroom.domain.ChatRoomType.GROUP;
import static com.coastee.server.global.util.PageableUtil.setSortOrder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupChatRoomFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomEntryService chatRoomEntryService;
    private final BlobStorageService blobStorageService;
    private final HashTagService hashTagService;

    @Transactional
    public void create(
            final Accessor accessor,
            final Long serverId,
            final CreateGroupChatRequest request,
            final MultipartFile image
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        ChatRoom chatRoom = chatRoomService.save(
                ChatRoom.groupChatRoom(server, user, request.getTitle(), request.getContent())
        );
        hashTagService.tag(chatRoom, request.getHashTags());
        if (image != null) {
            String imageUrl = blobStorageService.upload(image, DirName.GROUP, chatRoom.getId());
            chatRoom.updateThumbnail(imageUrl);
        }
        chatRoomEntryService.enter(user, chatRoom);
    }

    public ChatRoomElements findByScope(
            final Accessor accessor,
            final Long serverId,
            final Scope scope,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        if (scope.equals(Scope.owner)) {
            return findAllByServerAndOwner(server, user, setSortOrder(pageable));
        } else if (scope.equals(Scope.joined)) {
            return findAllByServerAndParticipant(server, user, setSortOrder(pageable));
        } else {
            return findAllByServer(server, user, setSortOrder(pageable));
        }
    }

    private ChatRoomElements findAllByServer(
            final Server server,
            final User currentUser,
            final Pageable pageable
    ) {
        Page<ChatRoom> chatRoomPage = chatRoomService.findAllByServerAndType(
                server,
                GROUP,
                pageable
        );
        Map<Long, Boolean> hasEnteredMap = chatRoomEntryService.findHasEnteredByChatRoomList(
                currentUser,
                chatRoomPage.getContent()
        );
        return ChatRoomElements.detail(chatRoomPage, hasEnteredMap);
    }

    private ChatRoomElements findAllByServerAndOwner(
            final Server server,
            final User user,
            final Pageable pageable
    ) {
        Page<ChatRoom> chatRoomPage = chatRoomService.findAllByServerAndUserAndType(
                server,
                user,
                GROUP,
                pageable
        );
        return ChatRoomElements.from(chatRoomPage);
    }

    private ChatRoomElements findAllByServerAndParticipant(
            final Server server,
            final User participant,
            final Pageable pageable
    ) {
        Page<ChatRoom> chatRoomPage = chatRoomService.findAllByServerAndParticipantAndType(
                server,
                participant,
                GROUP,
                pageable
        );
        return ChatRoomElements.from(chatRoomPage);
    }

    @Transactional
    public void enter(final Accessor accessor, final Long groupId) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(groupId);
        chatRoomEntryService.enter(user, chatRoom);
    }

    @Transactional
    public void exit(final Accessor accessor, final Long groupId) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(groupId);
        chatRoomEntryService.exit(user, chatRoom);
    }
}
