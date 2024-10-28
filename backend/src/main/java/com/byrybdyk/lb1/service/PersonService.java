package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository authorRepository) {
        this.personRepository = authorRepository;
    }

    public Person findById(Long authorId) {
        Optional<Person> personOptional = personRepository.findById(authorId);
        return personOptional.orElse(null);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

}