package com.coastee.server.dm.service;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.domain.repository.DMRepository;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.global.apipayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_DM_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMService {
    private final DMRepository dmRepository;

    public DirectMessage findById(final Long id) {
        return dmRepository.findById(id).orElseThrow(() -> new GeneralException(INVALID_DM_ID));
    }

    public Page<DirectMessage> findAllByDirectMessageRoom(
            final DirectMessageRoom dmRoom,
            final Pageable pageable
    ) {
        return dmRepository.findAllByDirectMessageRoom(dmRoom, pageable);
    }

    @Transactional
    public DirectMessage save(final DirectMessage directMessage) {
        return dmRepository.save(directMessage);
    }
}
