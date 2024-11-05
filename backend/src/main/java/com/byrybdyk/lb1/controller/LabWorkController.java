package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.model.Discipline;
import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.service.DisciplineService;
import com.byrybdyk.lb1.service.LabWorkService;
import com.byrybdyk.lb1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/labworks")
public class LabWorkController {

    private final DisciplineService disciplineService;
    private final PersonService authorService;
    private final LabWorkService labWorkService;

    @Autowired
    public LabWorkController(DisciplineService disciplineService, PersonService authorService, LabWorkService labWorkService) {
        this.disciplineService = disciplineService;
        this.authorService = authorService;
        this.labWorkService = labWorkService;
    }


    @PostMapping
    public ResponseEntity<LabWork> createLabWork(@RequestBody LabWorkDTO labWorkDTO) {
        try {
            System.out.println("Received LabWorkDTO: " + labWorkDTO);
            LabWork createdLabWork = labWorkService.createLabWorkFromDTO(labWorkDTO);
            System.out.println("2222");
            return new ResponseEntity<>(createdLabWork, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
