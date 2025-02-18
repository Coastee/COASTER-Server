package com.coastee.server.dm.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.domain.DMType;
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

import static com.coastee.server.global.domain.Constant.DMROOM_TOPIC_KEY;

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
    public void message(
            final Accessor accessor,
            final DMRequest dmRequest
    ) {
        dmRequest.validateUserId();
        User sender = userService.findById(accessor.getUserId());
        User receiver = userService.findById(dmRequest.getUserId());

        DirectMessageRoom dmRoom = dmRoomService.findOrCreateByUserAndUser(sender, receiver);
        dmRoomEntryService.enter(sender, dmRoom);
        dmRoomEntryService.enter(receiver, dmRoom);

        DirectMessage dm = dmService.save(dmRequest.toEntity(sender, dmRoom, DMType.TALK));
        redisTemplate.convertAndSend(
                channelTopicMap.get(DMROOM_TOPIC_KEY).getTopic(),
                new DMElement(dm)
        );
    }

    @Transactional
    public void message(
            final Accessor accessor,
            final Long roomId,
            final DMRequest dmRequest
    ) {
        User sender = userService.findById(accessor.getUserId());
        DirectMessageRoom dmRoom = dmRoomService.findById(roomId);
        dmRoomEntryService.validateJoin(sender, dmRoom);

        DirectMessage dm = dmService.save(dmRequest.toEntity(sender, dmRoom, DMType.TALK));
        redisTemplate.convertAndSend(
                channelTopicMap.get(DMROOM_TOPIC_KEY).getTopic(),
                new DMElement(dm)
        );
    }

    @Transactional
    public void deleteMessage(
            final Accessor accessor,
            final Long roomId,
            final Long dmId
    ) {
        User user = userService.findById(accessor.getUserId());
        DirectMessageRoom dmRoom = dmRoomService.findById(roomId);
        dmRoomEntryService.validateJoin(user, dmRoom);

        DirectMessage dm = dmService.findById(dmId);
        dm.validateUser(user);
        dm.delete();

        redisTemplate.convertAndSend(
                channelTopicMap.get(DMROOM_TOPIC_KEY).getTopic(),
                new DMElement(dm)
        );
    }
}
