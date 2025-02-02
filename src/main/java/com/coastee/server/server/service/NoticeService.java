package com.coastee.server.server.service;

import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Page<Notice> findAllByServer(final Server server, final Pageable pageable) {
        return noticeRepository.findAllByServer(server, pageable);
    }
}
