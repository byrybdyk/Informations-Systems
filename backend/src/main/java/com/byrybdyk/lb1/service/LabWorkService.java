package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.model.Discipline;
import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.repository.LabWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final DisciplineService disciplineService;
    private final PersonService authorService;

    @Autowired
    public LabWorkService(LabWorkRepository labWorkRepository, DisciplineService disciplineService, PersonService authorService) {
        this.labWorkRepository = labWorkRepository;
        this.disciplineService = disciplineService;
        this.authorService = authorService;

    }

    public LabWork saveLabWork(LabWork labWork) {
        return labWorkRepository.save(labWork);
    }

    public List<LabWork> getAllLabWorks() {
        return labWorkRepository.findAll();
    }

    private void mapDtoToLabWork(LabWork labWork, LabWorkDTO labWorkDTO, Person author) {
        labWork.setName(labWorkDTO.getName());
        labWork.setDescription(labWorkDTO.getDescription());
        labWork.setDifficulty(labWorkDTO.getDifficulty());
        labWork.setCoordinates(labWorkDTO.getCoordinates());
        labWork.setMinimalPoint(labWorkDTO.getMinimalPoint());
        labWork.setPersonalQualitiesMinimum(labWorkDTO.getPersonalQualitiesMinimum());
        labWork.setPersonalQualitiesMaximum(labWorkDTO.getPersonalQualitiesMaximum());
        labWork.setAuthor(author);
    }

    public LabWork createLabWorkFromDTO(LabWorkDTO labWorkDTO) {
        LabWork labWork = new LabWork();

        Person author = authorService.getOrCreateAuthor(labWorkDTO.getAuthorId(), labWorkDTO.getAuthor());
        labWork.setAuthor(author);

        Discipline discipline = disciplineService.getOrCreateDiscipline(labWorkDTO.getDiscipline());
        labWork.setDiscipline(discipline);

        mapDtoToLabWork(labWork, labWorkDTO, author);

        LabWork createdLabWork = saveLabWork(labWork);
        return createdLabWork;
    }

}
