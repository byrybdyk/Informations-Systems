package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.AdminRequest;
import com.byrybdyk.lb1.model.LabWorkHistory;
import com.byrybdyk.lb1.service.AdminRequestService;
import com.byrybdyk.lb1.service.LabWorkHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private final LabWorkHistoryService labWorkHistoryService;

    @Autowired
    public AdminController(AdminRequestService adminRequestService, LabWorkHistoryService labWorkHistoryService) {
        this.adminRequestService = adminRequestService;
        this.labWorkHistoryService = labWorkHistoryService;
    }

    @GetMapping("/requests")
    public String viewRequests(Model model) {
        List<AdminRequest> requests = adminRequestService.getAllRequests();
        model.addAttribute("requests", requests);
        return "admin-requests";
    }

    @GetMapping("/history")
    public String viewHistory(Model model, Authentication authentication) {
        List<LabWorkHistory> historyList = labWorkHistoryService.getAllHistory();
        model.addAttribute("historyList", historyList);
        String adminName = authentication.getName();
        model.addAttribute("username", adminName);
        return "admin-history";
    }

    @PostMapping("/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        adminRequestService.approveRequest(id);
        return "redirect:/admin/requests";
    }

}
