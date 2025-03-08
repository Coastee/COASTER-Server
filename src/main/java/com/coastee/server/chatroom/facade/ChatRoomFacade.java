package com.coastee.server.chatroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chat.service.ChatService;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.ChatRoomCreateRequest;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.service.HashTagService;
import com.coastee.server.image.domain.DirName;
import com.coastee.server.image.service.BlobStorageService;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.UserElements;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.coastee.server.global.util.PageableUtil.setChatOrder;
import static com.coastee.server.global.util.PageableUtil.setChatRoomOrder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomEntryService chatRoomEntryService;
    private final ChatService chatService;
    private final BlobStorageService blobStorageService;
    private final HashTagService hashTagService;

    @Transactional
    public void create(
            final Accessor accessor,
            final Long serverId,
            final ChatRoomType type,
            final ChatRoomCreateRequest request,
            final MultipartFile image
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        ChatRoom chatRoom = chatRoomService.save(request.toEntity(server, user, type));
        hashTagService.tag(chatRoom, request.getHashTags());
        if (image != null) {
            String imageUrl = blobStorageService.upload(image, DirName.CHATROOM, chatRoom.getId());
            chatRoom.updateThumbnail(imageUrl);
        }
        chatRoomEntryService.enter(user, chatRoom);
    }

    public ChatRoomElements findWithConditions(
            final Accessor accessor,
            final Long serverId,
            final ChatRoomType type,
            final Scope scope,
            final String keyword,
            final List<String> tagNameList,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        if (scope == Scope.owner) {
            return findAllByServerAndOwner(server, user, type, setChatRoomOrder(pageable));
        } else if (scope == Scope.joined) {
            return findAllByServerAndParticipant(server, user, type, setChatRoomOrder(pageable));
        } else {
            return findAllByServer(server, user, type, keyword, tagNameList, setChatRoomOrder(pageable));
        }
    }

    public ChatElements getChats(
            final Accessor accessor,
            final Long chatRoomId,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoomEntryService.validateJoin(user, chatRoom);
        Page<Chat> chatPage = chatService.findAllByChatRoom(chatRoom, setChatOrder(pageable));
        return new ChatElements(chatPage);
    }

    public UserElements getParticipants(
            final Accessor accessor,
            final Long chatRoomId,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoomEntryService.validateJoin(user, chatRoom);
        Page<ChatRoomEntry> entryPage = chatRoomEntryService.findAllByChatRoom(chatRoom, pageable);
        return UserElements.from(entryPage);
    }

    @Transactional
    public void toggleFavorite(final Accessor accessor, final Long chatRoomId) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoomEntryService.toggleFavorite(user, chatRoom);
    }

    @Transactional
    public void enter(final Accessor accessor, final Long chatRoomId) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoomEntryService.enter(user, chatRoom);
    }

    @Transactional
    public void exit(final Accessor accessor, final Long chatRoomId) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        chatRoomEntryService.exit(user, chatRoom);
    }

    private ChatRoomElements findAllByServerAndOwner(
            final Server server,
            final User user,
            final ChatRoomType type,
            final Pageable pageable
    ) {
        Page<ChatRoom> chatRoomPage = chatRoomService.findAllByServerAndUserAndType(
                server,
                user,
                type,
                pageable
        );
        return ChatRoomElements.from(chatRoomPage);
    }

    private ChatRoomElements findAllByServerAndParticipant(
            final Server server,
            final User participant,
            final ChatRoomType type,
            final Pageable pageable
    ) {
        Page<ChatRoom> chatRoomPage = chatRoomService.findAllByServerAndParticipantAndType(
                server,
                participant,
                type,
                pageable
        );
        return ChatRoomElements.from(chatRoomPage);
    }

    private ChatRoomElements findAllByServer(
            final Server server,
            final User currentUser,
            final ChatRoomType type,
            final String keyword,
            final List<String> tagNameList,
            final Pageable pageable
    ) {
        List<HashTag> hashTagList = new ArrayList<>();
        if (tagNameList != null && !tagNameList.isEmpty())
            hashTagList = hashTagService.findAllByContentIn(new HashSet<>(tagNameList));

        Page<ChatRoom> chatRoomPage = chatRoomService.findByServerAndTypeAndKeywordAndTagList(
                server,
                type,
                keyword,
                hashTagList,
                pageable
        );
        Map<Long, Boolean> hasEnteredMap = chatRoomEntryService.findHasEnteredByChatRoomList(
                currentUser,
                chatRoomPage.getContent()
        );
        return ChatRoomElements.detail(chatRoomPage, hasEnteredMap);
    }
}
