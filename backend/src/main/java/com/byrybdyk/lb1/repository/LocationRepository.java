package com.byrybdyk.lb1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.byrybdyk.lb1.model.Location;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
