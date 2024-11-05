package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.AdminRequest;
import com.byrybdyk.lb1.service.AdminRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminRequestService adminRequestService;

    @Autowired
    public AdminController(AdminRequestService adminRequestService) {
        this.adminRequestService = adminRequestService;
    }

    @GetMapping("/requests")
    public String viewRequests(Model model) {
        List<AdminRequest> requests = adminRequestService.getAllRequests();
        model.addAttribute("requests", requests);
        return "admin-requests";
    }

    @PostMapping("/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        adminRequestService.approveRequest(id);
        return "redirect:/admin/requests";
    }

}
