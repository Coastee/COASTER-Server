package com.coastee.server.server.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.server.domain.repository.ServerEntryRepository;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.coastee.server.global.BaseEntityStatus.ACTIVE;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NOT_IN_SERVER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerEntryService {
    private final ServerEntryRepository serverEntryRepository;

    @Transactional
    public void save(final User user, final List<Server> serverList) {
        List<ServerEntry> serverEntryList = serverList.stream()
                .map(server -> new ServerEntry(user, server)).toList();
        serverEntryRepository.saveAll(serverEntryList);
    }

    @Transactional
    public void exit(final User user, final Server server) {
        ServerEntry serverEntry = serverEntryRepository.findByUserAndServerAndStatus(user, server, ACTIVE)
                .orElseThrow(() -> new GeneralException(NOT_IN_SERVER));
        serverEntry.delete();
    }
}
