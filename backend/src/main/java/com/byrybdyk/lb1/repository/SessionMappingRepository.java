package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.SessionMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionMappingRepository extends JpaRepository<SessionMapping, Long> {
    Optional<SessionMapping> findByKeycloakSid(String keycloakSid);
    Optional<SessionMapping> findBySpringSessionId(String springSessionId);

    void deleteById(Long id);

    List<SessionMapping> findAllByKeycloakSid(String sessionId);
}
