package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
