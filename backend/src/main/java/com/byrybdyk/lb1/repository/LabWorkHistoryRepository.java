package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.LabWorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabWorkHistoryRepository extends JpaRepository<LabWorkHistory, Long> {
}
