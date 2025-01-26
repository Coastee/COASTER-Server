package com.coastee.server.server.service;

import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerEntryRepository;
import com.coastee.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final ServerEntryRepository serverEntryRepository;

    public List<Server> findAllById(final List<Long> idList) {
        return serverRepository.findAllById(idList);
    }
}
