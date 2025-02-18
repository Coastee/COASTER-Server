package com.coastee.server.dmroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.repository.dto.FindAnotherUserAndRecentDM;
import com.coastee.server.dmroom.dto.DMRoomElements;
import com.coastee.server.dmroom.service.DMRoomEntryService;
import com.coastee.server.dmroom.service.DMRoomService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DMRoomFacade {
    private final UserService userService;
    private final DMRoomService dmRoomService;
    private final DMRoomEntryService dmRoomEntryService;

    public DMRoomElements getRooms(final Accessor accessor, final Pageable pageable) {
        User user = userService.findById(accessor.getUserId());
        Page<DirectMessageRoom> dmRoomPage = dmRoomService.findByParticipant(user, pageable);
        Map<Long, FindAnotherUserAndRecentDM> dmRoomMap = dmRoomService.findAnotherUserAndDMByDMRoomListAndUser(
                dmRoomPage.getContent(),
                user
        );
        return new DMRoomElements(dmRoomPage, dmRoomMap);
    }
}
