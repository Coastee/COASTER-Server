package com.coastee.server.server.dto;

import com.coastee.server.server.domain.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ServerElements {
    private int count;
    private List<ServerElement> serverList;

    public ServerElements(List<Server> serverList) {
        this.count = serverList.size();
        this.serverList = serverList.stream().map(ServerElement::new).toList();
    }
}
