package com.coastee.server.dmroom.service;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.repository.DMRoomRepository;
import com.coastee.server.global.apipayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_DMROOM_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMRoomService {
    private final DMRoomRepository dmRoomRepository;

    public DirectMessageRoom findById(final Long roomId) {
        return dmRoomRepository.findById(roomId).orElseThrow(() -> new GeneralException(INVALID_DMROOM_ID));
    }

    @Transactional
    public DirectMessageRoom save(final DirectMessageRoom directMessageRoom) {
        return dmRoomRepository.save(directMessageRoom);
    }
}
