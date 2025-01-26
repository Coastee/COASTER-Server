package com.coastee.server.server.facade;

import com.coastee.server.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerFacade {
    private final ServerService serverService;
}
