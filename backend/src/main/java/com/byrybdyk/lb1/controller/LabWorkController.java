package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.service.LabWorkService;
import com.byrybdyk.lb1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labworks")
public class LabWorkController {

    @Autowired
    private final LabWorkService labWorkService;

    @Autowired
    private PersonService authorService;

    @Autowired
    public LabWorkController(LabWorkService labWorkService) {
        this.labWorkService = labWorkService;
    }

    @PostMapping
    public ResponseEntity<LabWork> createLabWork(@RequestBody LabWorkDTO labWorkDTO) {
        LabWork labWork = new LabWork();
        Person author = authorService.findById(labWorkDTO.getId());
        labWork.setName(labWorkDTO.getName());
        labWork.setDescription(labWorkDTO.getDescription());
        labWork.setDifficulty(labWorkDTO.getDifficulty());
        labWork.setAuthor(author);
        labWork.setCoordinates(labWorkDTO.getCoordinates());
        labWork.setCreationDate(labWorkDTO.getCreationDate());
        labWork.setDiscipline(labWorkDTO.getDiscipline());
        labWork.setMinimalPoint(labWorkDTO.getMinimalPoint());
        labWork.setPersonalQualitiesMinimum(labWorkDTO.getPersonalQualitiesMinimum());
        labWork.setPersonalQualitiesMaximum(labWorkDTO.getPersonalQualitiesMaximum());



        if (author != null) {
            labWork.setAuthor(author);
        } else {
            // Обработка случая, если автор не найден
            return new ResponseEntity<>(labWork, HttpStatus.METHOD_FAILURE);
        }

        LabWork createdLabWork = labWorkService.saveLabWork(labWork);
        return new ResponseEntity<>(createdLabWork, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LabWork>> getAllLabWorks() {
        List<LabWork> labWorks = labWorkService.getAllLabWorks();
        return new ResponseEntity<>(labWorks, HttpStatus.OK);
    }

}
