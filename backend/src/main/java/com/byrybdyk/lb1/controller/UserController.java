package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.LabWork;
import com.byrybdyk.lb1.service.LabWorkService;
import com.byrybdyk.lb1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showUserHome(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, Model model, Authentication authentication) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<LabWork> labWorksPage = labWorkService.getLabWorksPage(pageable);

        model.addAttribute("labWorks", labWorksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", labWorksPage.getTotalPages());

        String userName = authentication.getName();
        model.addAttribute("username", userName);

        return "user-home";
    }

    @GetMapping("/requests")
    public String showUserRequests(Model model, Authentication authentication) {

        String userName = authentication.getName();
        model.addAttribute("username", userName);

        return "user-request";
    }

}
