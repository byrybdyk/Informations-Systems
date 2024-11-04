package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.User;
import com.byrybdyk.lb1.model.enums.Role;
import com.byrybdyk.lb1.service.AdminRequestService;
import com.byrybdyk.lb1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRequestService adminRequestService;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AdminRequestService adminRequestService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.adminRequestService = adminRequestService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String role) {
        if (userService.findByUsername(username).isPresent()) {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
        }

        if ("ADMIN".equals(role) && userService.countAdmins() > 0) {
            adminRequestService.createRequest(username, passwordEncoder.encode(password));
            return new ResponseEntity<>("Admin registration request submitted successfully.", HttpStatus.CREATED);
        }

        userService.registerNewUser(username, password, Role.valueOf(role));
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return new ResponseEntity<>("Login successful.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
