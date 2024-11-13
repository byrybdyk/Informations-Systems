package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    List<LabWork> findByDescriptionStartingWith(String prefix);

    long countByAuthorNameLessThan(String authorName);

    void deleteByAuthor(Person author);
    List<LabWork> findByAuthor(Person author);
}
