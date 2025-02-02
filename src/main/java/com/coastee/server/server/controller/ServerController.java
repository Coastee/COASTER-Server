package com.coastee.server.server.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.server.dto.request.ServerEntryRequest;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.dto.response.ServerDetailResponse;
import com.coastee.server.server.facade.ServerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers")
public class ServerController {
    private final ServerFacade serverFacade;

    @GetMapping("")
    public ApiResponse<ServerElements> findAll() {
        return ApiResponse.onSuccess(serverFacade.findAll());
    }

    @GetMapping("/{serverId}")
    @UserOnly
    public ApiResponse<ServerDetailResponse> getHome(
            @Auth final Accessor accessor,
            @PathVariable final Long serverId
    ) {
        return ApiResponse.onSuccess(serverFacade.getHome(accessor, serverId));
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

    @PostMapping("/enter")
    @UserOnly
    public ApiResponse<Void> enterServer(
            @Auth final Accessor accessor,
            @RequestBody final ServerEntryRequest request
    ) {
        serverFacade.enter(accessor, request);
        return ApiResponse.onSuccess();
    }
}
