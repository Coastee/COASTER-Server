package com.coastee.server.user.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.dto.response.ScheduleElements;
import com.coastee.server.user.facade.ScheduleFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;

    @GetMapping("")
    @UserOnly
    public ApiResponse<ScheduleElements> getSchedule(
            @Auth final Accessor accessor,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(scheduleFacade.getSchedule(
                accessor,
                pageable
        ));
    }
}
