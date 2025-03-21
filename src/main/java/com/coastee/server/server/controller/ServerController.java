package com.coastee.server.server.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.dto.response.ServerHomeResponse;
import com.coastee.server.server.facade.ServerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers")
public class ServerController {
    private final ServerFacade serverFacade;

    @GetMapping("")
    public ApiResponse<ServerElements> findAll(
            @RequestParam(value = "scope", required = false) final Scope scope
    ) {
        return ApiResponse.onSuccess(serverFacade.findWithConditions(scope));
    }

    @GetMapping("/{serverId}")
    @UserOnly
    public ApiResponse<ServerHomeResponse> getHomeWithConditions(
            @Auth final Accessor accessor,
            @PathVariable final Long serverId,
            @RequestParam(value = "keyword", required = false) final String keyword,
            @RequestParam(value = "tags", required = false) final List<String> tagList
    ) {
        return ApiResponse.onSuccess(serverFacade.getHomeWithConditions(accessor, serverId, keyword.strip(), tagList));
    }

    @PostMapping("/{serverId}")
    @UserOnly
    public ApiResponse<Void> enterServer(
            @Auth final Accessor accessor,
            @PathVariable final Long serverId
    ) {
        serverFacade.enter(accessor, serverId);
        return ApiResponse.onSuccess();
    }

    @DeleteMapping("/{serverId}")
    @UserOnly
    public ApiResponse<Void> exitServer(
            @Auth final Accessor accessor,
            @PathVariable final Long serverId
    ) {
        serverFacade.exit(accessor, serverId);
        return ApiResponse.onSuccess();
    }
}
