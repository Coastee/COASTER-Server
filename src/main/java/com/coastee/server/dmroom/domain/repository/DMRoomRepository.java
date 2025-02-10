package com.coastee.server.dmroom.domain.repository;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRoomRepository extends JpaRepository<DirectMessageRoom, Long> {
}
