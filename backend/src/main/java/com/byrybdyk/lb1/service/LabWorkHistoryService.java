package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.LabWorkHistory;
import com.byrybdyk.lb1.repository.LabWorkHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabWorkHistoryService {

    private final LabWorkHistoryRepository labWorkHistoryRepository;

    @Autowired
    public LabWorkHistoryService(LabWorkHistoryRepository labWorkHistoryRepository) {
        this.labWorkHistoryRepository = labWorkHistoryRepository;
    }

    public List<LabWorkHistory> getAllHistory() {
        return labWorkHistoryRepository.findAll();
    }
}
