package com.coastee.server.server.dto;

import com.coastee.server.server.domain.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ServerElement {
    private Long id;
    private String title;

    public ServerElement(final Server server) {
        this.id = server.getId();
        this.title = server.getTitle();
    }
}
