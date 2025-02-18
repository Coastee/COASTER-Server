package com.coastee.server.dmroom.service;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.DirectMessageRoomEntry;
import com.coastee.server.dmroom.domain.repository.DMRoomEntryRepository;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NOT_IN_DMROOM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMRoomEntryService {
    private final DMRoomEntryRepository dmRoomEntryRepository;

    public DirectMessageRoomEntry validateJoin(final User user, final DirectMessageRoom dmRoom) {
        DirectMessageRoomEntry entry = dmRoomEntryRepository
                .findByUserAndDirectMessageRoom(user, dmRoom)
                .orElseThrow(() -> new GeneralException(NOT_IN_DMROOM));
        if (entry.isDeleted()) throw new GeneralException(NOT_IN_DMROOM);
        return entry;
    }

    @Transactional
    public DirectMessageRoomEntry enter(final User user, final DirectMessageRoom dmRoom) {
        DirectMessageRoomEntry entry = dmRoomEntryRepository
                .findByUserAndDirectMessageRoom(user, dmRoom)
                .orElse(createAndSave(user, dmRoom));
        entry.activate();
        return entry;
    }

    private DirectMessageRoomEntry createAndSave(final User user, final DirectMessageRoom dmRoom) {
        return dmRoomEntryRepository.save(new DirectMessageRoomEntry(user, dmRoom));
    }
}
