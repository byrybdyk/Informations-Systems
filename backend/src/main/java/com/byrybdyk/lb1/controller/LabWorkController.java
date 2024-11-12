    package com.byrybdyk.lb1.controller;

    import com.byrybdyk.lb1.dto.LabWorkDTO;
    import com.byrybdyk.lb1.model.LabWork;
    import com.byrybdyk.lb1.service.LabWorkService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.handler.annotation.MessageMapping;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;

    import java.util.Optional;

    @RestController
    @RequestMapping("/labworks")
    public class LabWorkController {

        private final LabWorkService labWorkService;
        private final SimpMessagingTemplate messagingTemplate;

        @Autowired
        public LabWorkController(LabWorkService labWorkService, SimpMessagingTemplate messagingTemplate) {
            this.labWorkService = labWorkService;
            this.messagingTemplate = messagingTemplate;
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
        public ResponseEntity<LabWork> updateLabWork(@RequestBody LabWorkDTO labWorkDTO) {
            try {

                LabWork createdLabWork = labWorkService.updateLabWorkFromDTO(labWorkDTO);

                System.out.println("Sending message to /topic/labworks: " + createdLabWork);
                messagingTemplate.convertAndSend("/topic/labworks", createdLabWork);
                System.out.println("Message sent to /topic/labworks.");

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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



    }
