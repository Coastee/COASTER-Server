package com.coastee.server.server.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.domain.BaseEntityStatus;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.server.domain.repository.ServerEntryRepository;
import com.coastee.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NOT_IN_ANY_SERVER;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.NOT_IN_SERVER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerEntryService {
    private final ServerEntryRepository serverEntryRepository;

    public List<ServerEntry> findByUser(final User user) {
        return serverEntryRepository.findByUserAndStatus(user, BaseEntityStatus.ACTIVE);
    }

    public ServerEntry findAnyByUser(final User user) {
        return serverEntryRepository.findFirstByUserAndStatus(user, BaseEntityStatus.ACTIVE)
                .orElseThrow(() -> new GeneralException(NOT_IN_ANY_SERVER));
    }

    @Transactional
    public ServerEntry enter(final User user, final Server server) {
        ServerEntry serverEntry = serverEntryRepository
                .findByUserAndServer(user, server)
                .orElseGet(() -> createAndSave(user, server));
        serverEntry.activate();
        return serverEntry;
    }

    private ServerEntry createAndSave(final User user, final Server server) {
        return serverEntryRepository.save(new ServerEntry(user, server));
    }

    @Transactional
    public void enter(final User user, final List<Server> serverList) {
        List<ServerEntry> existingEntries = serverEntryRepository
                .findByUserAndServerIn(user, serverList);
        existingEntries.forEach(ServerEntry::activate);

        Set<Server> existingServers = existingEntries.stream()
                .map(ServerEntry::getServer)
                .collect(Collectors.toSet());

        List<ServerEntry> newEntries = serverList.stream()
                .filter(server -> !existingServers.contains(server))
                .map(server -> new ServerEntry(user, server))
                .toList();

        serverEntryRepository.saveAll(newEntries);
    }

    @Transactional
    public void exit(final User user, final Server server) {
        ServerEntry serverEntry = validateJoin(user, server);
        serverEntry.delete();
    }

    public ServerEntry validateJoin(final User user, final Server server) {
        ServerEntry serverEntry = serverEntryRepository
                .findByUserAndServer(user, server)
                .orElseThrow(() -> new GeneralException(NOT_IN_SERVER));
        if (serverEntry.isDeleted()) throw new GeneralException(NOT_IN_SERVER);
        return serverEntry;
    }
}
