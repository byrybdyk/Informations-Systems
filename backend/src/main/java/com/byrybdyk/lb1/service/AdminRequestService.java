package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.AdminRequest;
import com.byrybdyk.lb1.model.User;
import com.byrybdyk.lb1.model.enums.Role;
import com.byrybdyk.lb1.repository.AdminRequestRepository;
import com.byrybdyk.lb1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRequestService(AdminRequestRepository adminRequestRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.adminRequestRepository = adminRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createRequest(String username, String password) {
        AdminRequest request = new AdminRequest(username, password);
        adminRequestRepository.save(request);
    }

    public void approveRequest(Long requestId) {
        AdminRequest request = adminRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setRole(Role.ADMIN);

        userRepository.save(newUser);

        adminRequestRepository.delete(request);
    }

    public List<AdminRequest> getAllRequests() {
        return adminRequestRepository.findAll();
    }
}
