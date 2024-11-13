    package com.byrybdyk.lb1.controller;

    import com.byrybdyk.lb1.dto.LabWorkDTO;
    import com.byrybdyk.lb1.model.*;
    import com.byrybdyk.lb1.service.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.handler.annotation.MessageMapping;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;

    import java.security.Principal;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/labworks")
    public class LabWorkController {

        private final LabWorkService labWorkService;
        private final SimpMessagingTemplate messagingTemplate;
        private final DisciplineService disciplineService;
        private final CoordinatesService coordinatesService;
        private final PersonService personService;
        private final LocationService locationService;


        @Autowired
        public LabWorkController(LabWorkService labWorkService, SimpMessagingTemplate messagingTemplate, PersonService personService, DisciplineService disciplineService, CoordinatesService coordinatesService, LocationService locationService) {
            this.labWorkService = labWorkService;
            this.messagingTemplate = messagingTemplate;
            this.personService = personService;
            this.disciplineService = disciplineService;
            this.coordinatesService = coordinatesService;
            this.locationService = locationService;
        }

        @GetMapping("/form-data")
        public ResponseEntity<?> getFormData() {
            List<Discipline> disciplines = disciplineService.findAll();
            List<Coordinates> coordinates = coordinatesService.findAll();
            List<Person> authors = personService.findAll();
            List<Location> locations = locationService.findAll();

            FormData formData = new FormData();
            formData.setDisciplines(disciplines);
            formData.setCoordinates(coordinates);
            formData.setAuthors(authors);
            formData.setLocations(locations);

            return new ResponseEntity<>(formData, HttpStatus.OK);
        }

        @MessageMapping("/labworks/add")
        public ResponseEntity<LabWork> createLabWork(@RequestBody LabWorkDTO labWorkDTO) {
            try {
                LabWork createdLabWork = labWorkService.createLabWorkFromDTO(labWorkDTO);

                System.out.println("Sending message to /topic/labworks: " + createdLabWork);
                messagingTemplate.convertAndSend("/topic/labworks", createdLabWork);
                System.out.println("Message sent to /topic/labworks.");

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @MessageMapping("/labworks/update")
        public ResponseEntity<LabWork> updateLabWork(@RequestBody LabWorkDTO labWorkDTO, Principal principal) {
            try {
                String username = principal.getName();
                LabWork createdLabWork = labWorkService.updateLabWorkFromDTO(labWorkDTO, username);

                System.out.println("Sending message to /topic/labworks: " + createdLabWork);
                messagingTemplate.convertAndSend("/topic/labworks", createdLabWork);
                System.out.println("Message sent to /topic/labworks.");

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @GetMapping("/{labWorkId}")
        public ResponseEntity<LabWork> getLabWorkById(@PathVariable Long labWorkId) {
            try {
                Optional<LabWork> labWorkOpt = labWorkService.findById(labWorkId);
                if (labWorkOpt.isPresent()) {
                    LabWork labWork = labWorkOpt.get();
                    return new ResponseEntity<>(labWork, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/{labWorkId}/can-edit")
        public ResponseEntity<Boolean> canEditLabWork(@PathVariable Long labWorkId, Authentication authentication) {
            try {
                String currentUsername = authentication.getName();
                System.out.println("Current username: " + currentUsername);

                Optional<LabWork> existingLabWorkOpt = labWorkService.findById(labWorkId);
                if (!existingLabWorkOpt.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                LabWork labWork = existingLabWorkOpt.get();
                System.out.println("LabWork ID: " + labWork.getId() + ", Owner ID: " + labWork.getOwner_id().getId());

                boolean canEdit = labWorkService.canThisUserEditLabWork(labWork, currentUsername);
                System.out.println("Can edit ПОЛУЧЕН" + canEdit);
                if (canEdit) {
                    return new ResponseEntity<>(true, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                System.out.println("Error while checking edit permissions: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @DeleteMapping("/deleteByAuthor/{author}")
        public ResponseEntity<String> deleteByAuthor(@PathVariable String author) {
            labWorkService.deleteByAuthor(author);
            return ResponseEntity.ok("LabWork with author " + author + " deleted.");
        }

        @GetMapping("/countByAuthor")
        public ResponseEntity<Long> countByAuthor(@RequestParam String author) {
            long count = labWorkService.countByAuthorLessThan(author);
            return ResponseEntity.ok(count);
        }

        @GetMapping("/findByDescriptionPrefix")
        public ResponseEntity<List<LabWork>> findByDescriptionPrefix(@RequestParam String prefix) {
            return ResponseEntity.ok(labWorkService.findByDescriptionPrefix(prefix));
        }

//        @PostMapping("/decreaseDifficulty")
//        public ResponseEntity<String> decreaseDifficulty(@RequestParam Long id, @RequestParam int steps) {
//            labWorkService.decreaseDifficulty(id, steps);
//            return ResponseEntity.ok("Difficulty decreased by " + steps + " steps.");
//        }
//
//        @DeleteMapping("/removeFromDiscipline")
//        public ResponseEntity<String> removeFromDiscipline(@RequestParam Long id) {
//            labWorkService.removeFromDiscipline(id);
//            return ResponseEntity.ok("LabWork removed from discipline.");
//        }



    }
