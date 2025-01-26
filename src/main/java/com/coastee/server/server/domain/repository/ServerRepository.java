package com.coastee.server.server.domain.repository;

import com.coastee.server.server.domain.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}
