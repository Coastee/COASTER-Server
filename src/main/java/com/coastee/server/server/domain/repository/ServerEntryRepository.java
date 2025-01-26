package com.coastee.server.server.domain.repository;

import com.coastee.server.server.domain.ServerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerEntryRepository extends JpaRepository<ServerEntry, Long> {
}
