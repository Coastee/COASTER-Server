package com.coastee.server.server.service;

import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.server.domain.repository.ServerEntryRepository;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
