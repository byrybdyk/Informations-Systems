//package com.byrybdyk.lb1.controller;
//
//import com.byrybdyk.lb1.dto.LabWorkDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/notifications")
//public class DataChangeNotificationController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    // Контроллер для уведомлений об изменении LabWork
//    @PostMapping("/labwork-update")
//    public void notifyLabWorkUpdate(@RequestBody LabWorkDTO labWorkDTO) {
//        // Отправка уведомления об изменении LabWork через WebSocket
//        messagingTemplate.convertAndSend("/topic/labworks", labWorkDTO);  // уведомляем на канале "/topic/labworks"
//    }
//}
