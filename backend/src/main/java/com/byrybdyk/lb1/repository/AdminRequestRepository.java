package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
}
