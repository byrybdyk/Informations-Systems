package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String authorName);
}
