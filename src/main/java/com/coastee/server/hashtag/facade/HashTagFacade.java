package com.coastee.server.hashtag.facade;

import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.dto.HashTagElements;
import com.coastee.server.hashtag.service.HashTagService;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashTagFacade {
    private final HashTagService hashTagService;
    private final ServerService serverService;

    public HashTagElements findWithConditions(
            final Long serverId,
            final String keyword,
            final Pageable pageable
    ) {
        Server server = serverService.findById(serverId);
        Page<HashTag> hashTagPage = hashTagService.findAllByKeywordAndServer(keyword, server, pageable);
        return new HashTagElements(hashTagPage);
    }
}
