package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.dto.PersonDTO;
import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Location;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping("/create")
    public ResponseEntity<Person> createPerson(@RequestBody PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setEyeColor(personDTO.getEyeColor());
        person.setHairColor(personDTO.getHairColor());

        Location location = new Location();
        location.setX(personDTO.getLocation().getX());
        location.setY(personDTO.getLocation().getY());
        location.setName(personDTO.getLocation().getName());
        person.setLocation(location);

        person.setWeight(personDTO.getWeight());
        person.setPassportID(personDTO.getPassportID());

        person.setDtype("DEFAULT_PERSON_TYPE");

        Person createdPerson = personService.savePerson(person);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }
}
