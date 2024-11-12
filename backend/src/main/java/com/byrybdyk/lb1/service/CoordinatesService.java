package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.Coordinates;
import com.byrybdyk.lb1.repository.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        }
        if (coordinatesData == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        Coordinates newCoordinates = coordinatesRepository.findById(coordinatesData.getId())
                .orElse(new Coordinates());
        newCoordinates.setX(coordinatesData.getX());
        newCoordinates.setY(coordinatesData.getY());
        return saveCoordinates(newCoordinates);
    }

    public Coordinates findById(Long coordinatesId) {
        Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(coordinatesId);
        return coordinatesOptional.orElse(null);
    }

    public Coordinates saveCoordinates(Coordinates coordinates) {
        return coordinatesRepository.save(coordinates);
    }
}
