package com.byrybdyk.lb1.repository;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    List<LabWork> findByDescriptionStartingWith(String prefix);

    long countByAuthorNameLessThan(String authorName);

    void deleteByAuthor(Person author);

    List<LabWork> findByAuthor(Person author);

    void deleteByAuthorName(String author);

    Optional<LabWork> findFirstByAuthorId(Long authorId);

    Page<LabWork> findAll(Specification<LabWork> spec, Pageable pageable);
}