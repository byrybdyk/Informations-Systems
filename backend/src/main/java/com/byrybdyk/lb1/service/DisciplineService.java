package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.Discipline;
import com.byrybdyk.lb1.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    // Найти дисциплину по ID
    public Discipline findById(Long id) {
        return disciplineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discipline with ID " + id + " not found"));
    }

    // Сохранить новую дисциплину
    public Discipline save(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }

    // Обновить дисциплину
    public Discipline update(Long id, Discipline updatedDiscipline) {
        Discipline existingDiscipline = findById(id);
        existingDiscipline.setName(updatedDiscipline.getName());
        existingDiscipline.setPracticeHours(updatedDiscipline.getPracticeHours());
        return disciplineRepository.save(existingDiscipline);
    }

    // Удалить дисциплину по ID
    public void delete(Long id) {
        disciplineRepository.deleteById(id);
    }

    // Получить все дисциплины
    public List<Discipline> findAll() {
        return disciplineRepository.findAll();
    }
}
