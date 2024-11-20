package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LabWorkRepository extends JpaRepository<LabWork, Long>, JpaSpecificationExecutor<LabWork> {

    List<LabWork> findByDescriptionStartingWith(String prefix);

    long countByAuthorNameLessThan(String authorName);

    void deleteByAuthor(Person author);
    List<LabWork> findByAuthor(Person author);

    void deleteByAuthorName(String author);
}
