package com.coastee.server.dm.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.dto.DMElement;
import com.coastee.server.dm.dto.request.DMRequest;
import com.coastee.server.dm.service.DMService;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.service.DMRoomEntryService;
import com.coastee.server.dmroom.service.DMRoomService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.coastee.server.global.domain.Constant.DM_CHANNEL_NAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMFacade {
    private final Map<String, ChannelTopic> channelTopicMap;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final DMService dmService;
    private final DMRoomService dmRoomService;
    private final DMRoomEntryService dmRoomEntryService;

    @Transactional
    public void message(final Accessor accessor, final DMRequest dmRequest) {
        User sender = userService.findById(accessor.getUserId());
        DirectMessageRoom dmRoom;
        if (dmRequest.getRoomId() != null) {
            dmRoom = dmRoomService.findById(dmRequest.getRoomId());
            dmRoomEntryService.validateJoin(sender, dmRoom);
        } else {
            User receiver = userService.findById(dmRequest.getUserId());
            // TODO: 이미 room이 있는지 확인 필요
            dmRoom = dmRoomService.save(new DirectMessageRoom(sender));
            dmRoomEntryService.enter(sender, dmRoom);
            dmRoomEntryService.enter(receiver, dmRoom);
        }
        DirectMessage dm = dmService.save(dmRequest.toEntity(sender, dmRoom));
        redisTemplate.convertAndSend(
                channelTopicMap.get(DM_CHANNEL_NAME).getTopic(),
                new DMElement(dm)
        );
    }
}
