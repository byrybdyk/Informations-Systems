package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.Location;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.repository.LocationRepository;
import com.byrybdyk.lb1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, LocationRepository locationRepository) {
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
    }

    public Person getOrCreateAuthor(Long authorId, Person authorData) {
        if (authorId != null) {
            Person existingAuthor = findById(authorId);
            if (existingAuthor != null) {
                return existingAuthor;
            }
            throw new IllegalArgumentException("Author with specified ID not found");
        }

        if (authorData != null && authorData.getId() > 0) {
            Person existingAuthor = findById(authorData.getId());
            if (existingAuthor != null) {
                return existingAuthor;
            }
            return savePerson(authorData);
        }

        if (authorData != null) {
            Location location = null;
            if (authorData.getLocation() != null && authorData.getLocation().getId() > 0) {
                location = locationRepository.findById(authorData.getLocation().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + authorData.getLocation().getId()));
            } else if (authorData.getLocation() != null) {
                location = locationRepository.save(authorData.getLocation());
            }
            authorData.setLocation(location);
            return savePerson(authorData);
        }

        throw new IllegalArgumentException("Invalid author data");
    }

    public Person findById(Long authorId) {
        Optional<Person> personOptional = personRepository.findById(authorId);
        return personOptional.orElse(null);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public long countByAuthorLessThan(Double weight) {
        return personRepository.countByWeightLessThan(weight);
    }
}