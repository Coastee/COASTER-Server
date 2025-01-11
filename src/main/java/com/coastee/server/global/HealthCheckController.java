package com.coastee.server.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/api/v1/health-check")
    public String healthCheck() {
        return "OK";
    }
}
