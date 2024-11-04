package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.repository.LabWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;

    @Autowired
    public LabWorkService(LabWorkRepository labWorkRepository) {
        this.labWorkRepository = labWorkRepository;
    }

    public LabWork saveLabWork(LabWork labWork) {
        return labWorkRepository.save(labWork);
    }

    public List<LabWork> getAllLabWorks() {
        return labWorkRepository.findAll();
    }

}
