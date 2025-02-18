package com.coastee.server.dm.domain.repository;

import com.coastee.server.dm.domain.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRepository extends JpaRepository<DirectMessage, Long> {
}
