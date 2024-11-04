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

    public Person getOrCreateAuthor(Long authorId, Person authorData) {
        if (authorId != null) {
            Person existingAuthor = findById(authorId);
            if (existingAuthor != null) {
                return existingAuthor;
            }
            throw new IllegalArgumentException("Author with specified ID not found");
        } else if (authorData != null) {
            return savePerson(authorData);
        } else {
            throw new IllegalArgumentException("Author data cannot be null");
        }
    }

    public Person findById(Long authorId) {
        Optional<Person> personOptional = personRepository.findById(authorId);
        return personOptional.orElse(null);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

}