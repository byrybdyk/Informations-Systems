package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.model.*;
import com.byrybdyk.lb1.model.enums.ChangeType;
import com.byrybdyk.lb1.model.enums.Difficulty;
import com.byrybdyk.lb1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final LabWorkHistoryRepository labWorkHistoryRepository;
    private final DisciplineService disciplineService;
    private final PersonService authorService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CoordinatesRepository coordinatesRepository;
    private final CoordinatesService coordinatesService;
    private final PersonRepository personRepository;

    @Autowired
    public LabWorkService(LabWorkRepository labWorkRepository, LabWorkHistoryRepository labWorkHistoryRepository, DisciplineService disciplineService, PersonService authorService, UserRepository userRepository, UserService userService, CoordinatesRepository coordinatesRepository, CoordinatesService coordinatesService, PersonRepository personRepository) {
        this.labWorkRepository = labWorkRepository;
        this.labWorkHistoryRepository = labWorkHistoryRepository;
        this.disciplineService = disciplineService;
        this.authorService = authorService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.coordinatesRepository = coordinatesRepository;
        this.coordinatesService = coordinatesService;
        this.personRepository = personRepository;
    }

    public LabWork saveLabWork(LabWork labWork) {
        LabWork savedLabWork = labWorkRepository.save(labWork);
        return savedLabWork;
    }

    public List<LabWork> getAllLabWorks() {
        return labWorkRepository.findAll();
    }

    private void mapDtoToLabWork(LabWork labWork, LabWorkDTO labWorkDTO, Person author, User owner) {
        labWork.setName(labWorkDTO.getName());
        labWork.setDescription(labWorkDTO.getDescription());
        labWork.setDifficulty(labWorkDTO.getDifficulty());
        labWork.setMinimalPoint(labWorkDTO.getMinimalPoint());
        labWork.setPersonalQualitiesMinimum(labWorkDTO.getPersonalQualitiesMinimum());
        labWork.setPersonalQualitiesMaximum(labWorkDTO.getPersonalQualitiesMaximum());
        labWork.setAuthor(author);
        labWork.setOwner_id(owner);
    }

    public LabWork createLabWorkFromDTO(LabWorkDTO labWorkDTO) {
        try {
            LabWork labWork = new LabWork();

//            System.out.println("AuthorId " + labWorkDTO.getAuthor().getId() + " AuthorID " + labWorkDTO.getAuthorId());
            Person author = authorService.getOrCreateAuthor(labWorkDTO.getAuthorId() , labWorkDTO.getAuthor());
            labWork.setAuthor(author);

            Discipline discipline = disciplineService.getOrCreateDiscipline(labWorkDTO.getDiscipline());
            labWork.setDiscipline(discipline);

//            System.out.println("CoordinatesId " + labWorkDTO.getCoordinates().getId() + " CoordinatesId " + labWorkDTO.getCoordinatesId());
            Coordinates coordinates = coordinatesService.getOrCreateCoordinates(labWorkDTO.getCoordinatesId(), labWorkDTO.getCoordinates());
            labWork.setCoordinates(coordinates);

            String userName = labWorkDTO.getOwnerName();
            User owner = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            mapDtoToLabWork(labWork, labWorkDTO, author, owner);
            LabWork savedLabWork = saveLabWork(labWork);

            User currentUser = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            saveLabWorkHistory(savedLabWork, ChangeType.CREATE, currentUser);

            return savedLabWork;
        } catch (Exception e) {
            System.out.println("Error while creating LabWork: " + e.getMessage());
            throw e;
        }
    }

    public LabWork updateLabWorkFromDTO(LabWorkDTO labWorkDTO, String currentUserName) {
        try {
            Optional<LabWork> existingLabWorkOpt = labWorkRepository.findById(labWorkDTO.getId());

            if (!existingLabWorkOpt.isPresent()) {
                throw new IllegalArgumentException("LabWork not found for ID: " + labWorkDTO.getId());
            }

            LabWork labWork = existingLabWorkOpt.get();

            Person author = authorService.getOrCreateAuthor(labWorkDTO.getAuthorId(), labWorkDTO.getAuthor());
            labWork.setAuthor(author);

            Discipline discipline = disciplineService.getOrCreateDiscipline(labWorkDTO.getDiscipline());
            labWork.setDiscipline(discipline);

            Coordinates coordinates = coordinatesService.getOrCreateCoordinates(labWorkDTO.getCoordinatesId(), labWorkDTO.getCoordinates());
            labWork.setCoordinates(coordinates);

            String userName = labWorkDTO.getOwnerName();
            System.out.println("username: " + userName);
            User owner = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            mapDtoToLabWork(labWork, labWorkDTO, author, owner);

            LabWork updatedLabWork = saveLabWork(labWork);

            User currentUser = userRepository.findByUsername(currentUserName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            saveLabWorkHistory(updatedLabWork, ChangeType.UPDATE, currentUser);

            return updatedLabWork;
        } catch (Exception e) {
            System.out.println("Error while updating LabWork: " + e.getMessage());
            throw e;
        }
    }

    public void deleteLabWork(Long labWorkId) {
        Optional<LabWork> existingLabWorkOpt = labWorkRepository.findById(labWorkId);
        if (existingLabWorkOpt.isPresent()) {
            LabWork labWork = existingLabWorkOpt.get();

            User currentUser = userService.getCurrentAuthenticatedUser();
            saveLabWorkHistory(labWork, ChangeType.DELETE, currentUser);

            labWorkRepository.delete(labWork);
        }
    }

    public boolean canThisUserEditLabWork(LabWork labWork, String currentUsername) {
        try {
            User currentUser = userService.getUserByUsername(currentUsername);
            return labWork.getOwner_id().getId() == currentUser.getId() || currentUser.getRole().equals(currentUser.getRole().ADMIN);
        } catch (Exception e) {
            System.out.println("Error while checking edit permissions: " + e.getMessage());
            return false;
        }
    }

    public Optional<LabWork> findById(Long labWorkId) {
        return labWorkRepository.findById(labWorkId);
    }

    private void saveLabWorkHistory(LabWork labWork, ChangeType changeType, User changedBy) {
        LabWorkHistory history = new LabWorkHistory();
        history.setName(labWork.getName());
        history.setCreationDate(labWork.getCreationDate());
        history.setDescription(labWork.getDescription());
        history.setDifficulty(labWork.getDifficulty());
        history.setDiscipline(labWork.getDiscipline());
        history.setMinimalPoint(labWork.getMinimalPoint());
        history.setPersonalQualitiesMinimum(labWork.getPersonalQualitiesMinimum());
        history.setPersonalQualitiesMaximum(labWork.getPersonalQualitiesMaximum());
        history.setAuthor(labWork.getAuthor());
        history.setOwner_id(labWork.getOwner_id());
        history.setChangeType(changeType);
        history.setChangedBy(changedBy);
        history.setCoordinates(labWork.getCoordinates());
        history.setUpdateTime(LocalDateTime.now());
        labWorkHistoryRepository.save(history);
    }

    public void deleteByAuthor(String author) {
        // Извлекаем Person из Optional, если он существует
        Person person = personRepository.findByName(author)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        // Теперь передаем найденного человека в репозиторий LabWork
        List<LabWork> labWorks = labWorkRepository.findByAuthor(person);
        if (!labWorks.isEmpty()) {
            labWorkRepository.deleteAll(labWorks);
        }
    }


    public long countByAuthorLessThan(String authorName) {
        return labWorkRepository.countByAuthorNameLessThan(authorName);
    }


    public List<LabWork> findByDescriptionPrefix(String prefix) {
        return labWorkRepository.findByDescriptionStartingWith(prefix);
    }


    @Transactional
    public void decreaseDifficulty(Long labWorkId, int steps) {
        LabWork labWork = labWorkRepository.findById(labWorkId)
                .orElseThrow(() -> new IllegalArgumentException("LabWork not found with id: " + labWorkId));

        // Понижаем сложность на указанное число шагов, но не ниже минимального значения
        int newDifficultyLevel = Math.max(labWork.getDifficulty().ordinal() - steps, 0);
        labWork.setDifficulty(Difficulty.values()[newDifficultyLevel]);

        labWorkRepository.save(labWork);
    }


    @Transactional
    public void removeFromDiscipline(Long labWorkId) {
        LabWork labWork = labWorkRepository.findById(labWorkId)
                .orElseThrow(() -> new IllegalArgumentException("LabWork not found with id: " + labWorkId));

        // Удаляем связь с дисциплиной
        labWork.setDiscipline(null);
        labWorkRepository.save(labWork);
    }
}
