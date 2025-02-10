package com.coastee.server.dm.service;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.domain.repository.DMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMService {
    private final DMRepository dmRepository;

    @Transactional
    public DirectMessage save(final DirectMessage directMessage) {
        return dmRepository.save(directMessage);
    }
}
