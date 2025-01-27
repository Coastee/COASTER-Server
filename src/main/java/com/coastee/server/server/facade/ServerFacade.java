package com.coastee.server.server.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.dto.request.ServerEntryRequest;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.service.ServerEntryService;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServerFacade {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerEntryService serverEntryService;

    public ServerElements findAll() {
        List<Server> serverList = serverService.findAll();
        return new ServerElements(serverList);
    }

    @Transactional
    public void enter(final Accessor accessor, final ServerEntryRequest request) {
        User user = userService.findById(accessor.getUserId());
        List<Server> serverList = serverService.findAllById(request.getIdList());
        serverEntryService.save(user, serverList);
    }

    @Transactional
    public void exit(final Accessor accessor, final Long serverId) {
        User user = userService.findById(accessor.getUserId());
        Server server = serverService.findById(serverId);
        serverEntryService.exit(user, server);
    }
}
