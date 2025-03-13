package com.coastee.server.user.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.response.ScheduleElements;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleFacade {
    final UserService userService;
    final ChatRoomService chatRoomService;

    public ScheduleElements getSchedule(
            final Accessor accessor,
            final Pageable pageable
    ) {
        User user = userService.findById(accessor.getUserId());
        Page<ChatRoom> chatRoomPage = chatRoomService
                .findByUserAndType(user, ChatRoomType.MEETING, pageable);
        return new ScheduleElements(chatRoomPage);
    }
}
