package com.coastee.server.server.service;

import com.coastee.server.server.domain.repository.ServerEntryRepository;
import com.coastee.server.server.domain.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final ServerEntryRepository serverEntryRepository;
}
