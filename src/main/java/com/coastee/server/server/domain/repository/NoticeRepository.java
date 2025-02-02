package com.coastee.server.server.domain.repository;

import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAllByServer(final Server server, final Pageable pageable);
}
