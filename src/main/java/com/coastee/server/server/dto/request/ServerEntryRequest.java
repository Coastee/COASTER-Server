package com.coastee.server.server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ServerEntryRequest {
    private List<Long> serverIdList;
}
