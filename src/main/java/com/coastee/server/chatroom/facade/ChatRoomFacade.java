package com.coastee.server.chatroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.dto.request.CreateChatRoomRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomFacade {
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
            final CreateChatRoomRequest request,
            final MultipartFile image
    ) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        ChatRoom chatRoom = chatRoomService.save(request.toEntity(server, user));
        hashTagService.tag(chatRoom, request.getHashTags());
        if (image != null) {
            String imageUrl = blobStorageService.upload(image, DirName.CHATROOM, chatRoom.getId());
            chatRoom.updateThumbnail(imageUrl);
        }
        chatRoomEntryService.enter(user, chatRoom);
    }
}
