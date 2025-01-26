package com.coastee.server.server.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.server.dto.request.ServerEntryRequest;
import com.coastee.server.server.facade.ServerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers")
public class ServerController {
    private final ServerFacade serverFacade;

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
