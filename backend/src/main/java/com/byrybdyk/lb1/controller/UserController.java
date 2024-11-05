package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.service.LabWorkService;
import com.byrybdyk.lb1.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final LabWorkService labWorkService;
    private final UserService userService;

    @Autowired
    public UserController(LabWorkService labWorkService, UserService userService) {
        this.labWorkService = labWorkService;
        this.userService = userService;

    }

    @GetMapping("/home")
    public String showUserHome(Model model, Authentication authentication) {
        List<LabWork> labWorks = labWorkService.getAllLabWorks();
        model.addAttribute("labWorks", labWorks);

        String userName = authentication.getName();
        model.addAttribute("username", userName);

        return "user-home";
    }

}
