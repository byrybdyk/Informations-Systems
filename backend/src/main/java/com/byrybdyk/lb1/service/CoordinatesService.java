package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.Coordinates;
import com.byrybdyk.lb1.repository.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    public Coordinates getOrCreateCoordinates(Long coordinatesId, Coordinates coordinatesData) {
        if (coordinatesId != null) {
            Coordinates existingCoordinates = findById(coordinatesId);
            if (existingCoordinates != null) {
                return existingCoordinates;
            }
            throw new IllegalArgumentException("Coordinates with specified ID not found");
        } else if (coordinatesData.getId() > 0) {
            Coordinates existingCoordinates2 = findById(coordinatesData.getId());
            if (existingCoordinates2 != null) {
                return existingCoordinates2;
            }
        } else if (coordinatesData != null) {
            return saveCoordinates(coordinatesData);
        }
        else  {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        throw new IllegalArgumentException("Coordinates cannot be null");
    }

    public Coordinates findById(Long coordinatesId) {
        Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(coordinatesId);
        return coordinatesOptional.orElse(null);
    }

    public Coordinates saveCoordinates(Coordinates coordinates) {
        return coordinatesRepository.save(coordinates);
    }

    public List<Coordinates> findAll() {
        return coordinatesRepository.findAll();
    }
}
