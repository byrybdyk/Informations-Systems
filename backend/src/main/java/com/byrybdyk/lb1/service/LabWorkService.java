package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.dto.LabWorkDTO;
import com.byrybdyk.lb1.model.Discipline;
import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.model.Person;
import com.byrybdyk.lb1.model.User;
import com.byrybdyk.lb1.repository.LabWorkRepository;
import com.byrybdyk.lb1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final DisciplineService disciplineService;
    private final PersonService authorService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LabWorkService(LabWorkRepository labWorkRepository, DisciplineService disciplineService, PersonService authorService, UserRepository userRepository, UserService userService) {
        this.labWorkRepository = labWorkRepository;
        this.disciplineService = disciplineService;
        this.authorService = authorService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public LabWork saveLabWork(LabWork labWork) {
        return labWorkRepository.save(labWork);
    }

    public List<LabWork> getAllLabWorks() {
        return labWorkRepository.findAll();
    }

    private void mapDtoToLabWork(LabWork labWork, LabWorkDTO labWorkDTO, Person author, User owner) {
        labWork.setName(labWorkDTO.getName());
        labWork.setDescription(labWorkDTO.getDescription());
        labWork.setDifficulty(labWorkDTO.getDifficulty());
        labWork.setCoordinates(labWorkDTO.getCoordinates());
        labWork.setMinimalPoint(labWorkDTO.getMinimalPoint());
        labWork.setPersonalQualitiesMinimum(labWorkDTO.getPersonalQualitiesMinimum());
        labWork.setPersonalQualitiesMaximum(labWorkDTO.getPersonalQualitiesMaximum());
        labWork.setAuthor(author);
        labWork.setOwner_id(owner);
    }

    public LabWork createLabWorkFromDTO(LabWorkDTO labWorkDTO) {
        try {
            LabWork labWork = new LabWork();
            Person author = authorService.getOrCreateAuthor(labWorkDTO.getAuthorId(), labWorkDTO.getAuthor());
            labWork.setAuthor(author);

            Discipline discipline = disciplineService.getOrCreateDiscipline(labWorkDTO.getDiscipline());
            labWork.setDiscipline(discipline);

            String userName = labWorkDTO.getOwnerName();
            System.out.println("userName: " + userName);
            User owner = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            mapDtoToLabWork(labWork, labWorkDTO, author, owner);
            saveLabWork(labWork);
            return labWork;
        } catch (Exception e) {
            System.out.println("Error while creating LabWork: " + e.getMessage());
            throw e;
        }
    }

    public LabWork updateLabWorkFromDTO(LabWorkDTO labWorkDTO) {
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

            String userName = labWorkDTO.getOwnerName();
            System.out.println("userName: " + userName);
            User owner = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            mapDtoToLabWork(labWork, labWorkDTO, author, owner);

            saveLabWork(labWork);

            return labWork;
        } catch (Exception e) {
            System.out.println("Error while updating LabWork: " + e.getMessage());
            throw e;
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
}
